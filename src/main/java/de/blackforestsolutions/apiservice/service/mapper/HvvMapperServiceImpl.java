package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.CurrencyConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import de.blackforestsolutions.generatedcontent.hvv.response.Coordinate;
import de.blackforestsolutions.generatedcontent.hvv.response.HvvStationList;
import de.blackforestsolutions.generatedcontent.hvv.response.HvvTravelPointResponse;
import de.blackforestsolutions.generatedcontent.hvv.response.journey.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.checkIfStringPropertyExists;
import static de.blackforestsolutions.apiservice.service.mapper.MapperService.generateDurationFromStartToDestination;
import static de.blackforestsolutions.apiservice.service.mapper.MapperService.setPriceForLegBy;

@Service
@Slf4j
public class HvvMapperServiceImpl implements HvvMapperService {

    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;

    private enum HvvVehicleType {
        BUS(VehicleType.BUS),
        TRAIN(VehicleType.TRAIN),
        SHIP(VehicleType.FERRY),
        FOOTPATH(VehicleType.WALK),
        BICYCLE(VehicleType.BIKE),
        AIRPLANE(VehicleType.PLANE),
        CHANGE(VehicleType.WALK),
        CHANGE_SAME_PLATFORM(VehicleType.WALK);

        private final VehicleType vehicleType;

        HvvVehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
        }

        VehicleType getVehicleType() {
            return vehicleType;
        }
    }

    private final UuidService uuidService;

    @Autowired
    public HvvMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    @Override
    public HvvStation getHvvStationFrom(String jsonString) {
        return mapHvvTravelPointResponseToHvvStation(retrieveHvvTravelPointResponse(jsonString));
    }

    @Override
    public List<TravelPoint> getStationListFrom(String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HvvStationList hvvStationList = mapper.readValue(jsonBody, HvvStationList.class);
        return mapHvvStationListToTravelPointList(hvvStationList);
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneyMapFrom(String jsonBody) {
        return mapHvvRouteToJourneyMap(retrieveHvvRouteStatus(jsonBody));
    }

    private static HvvTravelPointResponse retrieveHvvTravelPointResponse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(jsonString, HvvTravelPointResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map json string due to mapping problems: {}", jsonString, e);
            return new HvvTravelPointResponse();
        }
    }

    private static TravelLine buildTravelLineWith(ScheduleElement tripBetween) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setOrigin(buildTravelPointWith(tripBetween.getLine().getOrigin()));
        travelLine.setBetweenHolds(buildBetweenHoldsForBetweenTrip(tripBetween.getIntermediateStops()));
        travelLine.setDirection(buildTravelPointWith(tripBetween.getLine().getDirection()));
        return travelLine.build();
    }

    private static TravelPoint buildTravelPointWith(String stationName) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName(stationName);
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    private static Map<Integer, TravelPoint> buildBetweenHoldsForBetweenTrip(List<From> intermediateStops) {
        AtomicInteger counter = new AtomicInteger(0);
        return intermediateStops
                .stream()
                .map(intermediateStop -> {
                    TravelPoint.TravelPointBuilder travelPoint = buildTravelPointWith(intermediateStop);
                    return extendTravelPointWithArrDepTime(travelPoint, intermediateStop);
                })
                .collect(Collectors.toMap(travelPoint -> counter.getAndIncrement(), travelPoint -> travelPoint));
    }

    private static Date generateDateFromDateAndTime(String date, String time) {
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate datePart = LocalDate.parse(date, dTF);
        LocalTime timePart = LocalTime.parse(time);
        LocalDateTime dateTime = LocalDateTime.of(datePart, timePart);
        return convertLocalDateTimeToDate(dateTime);
    }

    private static Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Price buildPriceFrom(List<TicketInfo> ticketInfos) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol(CurrencyConfiguration.EURO);
        price.setValues(Map.of(
                PriceCategory.ADULT, BigDecimal.valueOf(ticketInfos.get(FIRST_INDEX).getBasePrice()),
                PriceCategory.ADULT_REDUCED, BigDecimal.valueOf(ticketInfos.get(FIRST_INDEX).getReducedBasePrice()),
                PriceCategory.CHILD, BigDecimal.valueOf(ticketInfos.get(SECOND_INDEX).getBasePrice()),
                PriceCategory.CHILD_REDUCED, BigDecimal.valueOf(ticketInfos.get(SECOND_INDEX).getReducedBasePrice())
        ));
        price.setAffiliateLinks(Map.of(
                PriceCategory.ADULT, ticketInfos.get(FIRST_INDEX).getShopLinkRegular(),
                PriceCategory.ADULT_REDUCED, ticketInfos.get(FIRST_INDEX).getShopLinkRegular(),
                PriceCategory.CHILD, ticketInfos.get(SECOND_INDEX).getShopLinkRegular(),
                PriceCategory.CHILD_REDUCED, ticketInfos.get(SECOND_INDEX).getShopLinkRegular()
        ));
        return price.build();
    }

    private static TravelPoint.TravelPointBuilder buildTravelPointWith(From hvvStation) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setPlatform(checkIfStringPropertyExists(hvvStation.getPlatform()));
        travelPoint.setStationId(hvvStation.getId());
        Optional.ofNullable(hvvStation.getCombinedName()).ifPresentOrElse(travelPoint::setStationName, () -> travelPoint.setStationName(hvvStation.getName()));
        travelPoint.setCity(hvvStation.getCity());
        travelPoint.setCountry(Locale.GERMANY);
        Optional.ofNullable(hvvStation.getCoordinate()).ifPresent(coord -> travelPoint.setGpsCoordinates(mapHvvCoordinatesToBsCoordinates(coord)));
        return travelPoint;
    }

    private static TravelPoint extendTravelPointWithArrDepTime(TravelPoint.TravelPointBuilder travelPoint, From hvvStation) {
        Optional.ofNullable(hvvStation.getArrTime()).ifPresent(arrTime -> travelPoint.setArrivalTime(
                generateDateFromDateAndTime(arrTime.getDate(), arrTime.getTime())
        ));
        Optional.ofNullable(hvvStation.getDepTime()).ifPresent(depTime -> travelPoint.setDepartureTime(
                generateDateFromDateAndTime(depTime.getDate(), depTime.getTime())
        ));
        return travelPoint.build();
    }

    private static Coordinates mapHvvCoordinatesToBsCoordinates(Coordinate coordinate) {
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLatitude(coordinate.getY());
        coordinates.setLongitude(coordinate.getX());
        return coordinates.build();
    }

    private CallStatus retrieveHvvRouteStatus(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return new CallStatus(mapper.readValue(jsonString, HvvRoute.class), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping json to object: {}", jsonString, e);
            return new CallStatus(null, Status.FAILED, e);
        }
    }

    private HvvStation mapHvvTravelPointResponseToHvvStation(HvvTravelPointResponse travelPointResponse) {
        HvvStation hvvStation = new HvvStation();
        BeanUtils.copyProperties(travelPointResponse.getResults().get(FIRST_INDEX), hvvStation);
        return hvvStation;
    }

    private Map<UUID, JourneyStatus> mapHvvRouteToJourneyMap(CallStatus callStatus) {
        if (callStatus.getCalledObject() != null) {
            HvvRoute hvvRoute = (HvvRoute) callStatus.getCalledObject();
            return hvvRoute
                    .getRealtimeSchedules()
                    .stream()
                    .map(this::mapRealtimeScheduleToJourney)
                    .collect(Collectors.toMap(Journey::getId, JourneyStatusBuilder::createJourneyStatusWith));
        } else {
            return Map.of(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(callStatus.getException()));
        }
    }

    private Journey mapRealtimeScheduleToJourney(RealtimeSchedule realtimeSchedule) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        journey.setLegs(buildLegsWith(realtimeSchedule.getScheduleElements(), buildPriceFrom(realtimeSchedule.getTariffInfos().get(FIRST_INDEX).getTicketInfos())));
        return journey.build();
    }

    private LinkedHashMap<UUID, Leg> buildLegsWith(List<ScheduleElement> legs, Price price) {
        AtomicInteger counter = new AtomicInteger(0);
        return legs
                .stream()
                .map(leg -> buildLegWith(leg, price, counter.getAndIncrement()))
                .collect(Collectors.toMap(Leg::getId, leg -> leg, (prev, next) -> next, LinkedHashMap::new));
    }

    private Leg buildLegWith(ScheduleElement scheduleElement, Price price, int index) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setStart(buildTravelPointWith(scheduleElement.getFrom()).build());
        leg.setDestination(buildTravelPointWith(scheduleElement.getTo()).build());
        leg.setStartTime(generateDateFromDateAndTime(
                scheduleElement.getFrom().getDepTime().getDate(),
                scheduleElement.getFrom().getDepTime().getTime())
        );
        leg.setArrivalTime(generateDateFromDateAndTime(
                scheduleElement.getTo().getArrTime().getDate(),
                scheduleElement.getTo().getArrTime().getTime())
        );
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleName(scheduleElement.getLine().getName());
        String vehicleType = scheduleElement.getLine().getType().getSimpleType();
        leg.setVehicleType(getVehicleType(vehicleType));
        leg.setIncidents(scheduleElement.getAttributes().stream()
                .map(Attribute::getValue)
                .collect(Collectors.toList()));
        setTravelLineAndProviderId(leg, scheduleElement);
        setPriceForLegBy(index, leg, price);
        return leg.build();
    }

    private void setTravelLineAndProviderId(Leg.LegBuilder leg, ScheduleElement scheduleElement) {
        if (leg.getVehicleType() != VehicleType.WALK && leg.getVehicleType() != VehicleType.BIKE) {
            leg.setTravelLine(buildTravelLineWith(scheduleElement));
            leg.setProviderId(scheduleElement.getLine().getId());
        }
    }

    private List<TravelPoint> mapHvvStationListToTravelPointList(HvvStationList hvvStationList) {
        return hvvStationList.getStations()
                .stream()
                .map(station -> {
                    From hvvStation = new From();
                    BeanUtils.copyProperties(station, hvvStation);
                    return buildTravelPointWith(hvvStation).build();
                })
                .collect(Collectors.toList());
    }

    private VehicleType getVehicleType(String vehicleType) {
        return Arrays.stream(HvvVehicleType.values())
                .filter(hvvVehicleType -> hvvVehicleType.name().equals(vehicleType))
                .findFirst()
                .map(HvvVehicleType::getVehicleType)
                .orElse(null);
    }
}
