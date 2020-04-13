package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.checkIfStringPropertyExists;

@Service
@Slf4j
public class HvvMapperServiceImpl implements HvvMapperService {

    private static final int FIRST = 0;

    private final UuidService uuidService;

    @Autowired
    public HvvMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
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
                .map(intermediateStop -> buildTravelPointWith(intermediateStop).build())
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

    private static Price buildPriceFrom(TicketInfo ticketInfo) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValue(ticketInfo.getBasePrice());
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol("€");
        price.setAffiliateLink(ticketInfo.getShopLinkRegular());
        return price.build();
    }

    private static TravelPoint.TravelPointBuilder buildTravelPointWith(From hvvStation) {
        TravelPoint.TravelPointBuilder travelPoint = buildTravelPointWith(hvvStation.getId(), hvvStation.getCombinedName(), hvvStation.getName(), hvvStation.getCity(), hvvStation.getCoordinate(), hvvStation.getServiceTypes());
        Optional.ofNullable(hvvStation.getArrTime()).ifPresent(arrTime -> travelPoint.setArrivalTime(
                generateDateFromDateAndTime(arrTime.getDate(), arrTime.getTime())
        ));
        Optional.ofNullable(hvvStation.getDepTime()).ifPresent(depTime -> travelPoint.setDepartureTime(
                generateDateFromDateAndTime(depTime.getDate(), depTime.getTime())
        ));
        travelPoint.setVehicleTypes(hvvStation.getServiceTypes());
        travelPoint.setPlatform(checkIfStringPropertyExists(hvvStation.getPlatform()));
        return travelPoint;
    }

    private static TravelPoint.TravelPointBuilder buildTravelPointWith(String id, String combinedName, String name, String city, Coordinate coordinate, List<String> vehicleTypes) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId(id);
        Optional.ofNullable(combinedName).ifPresentOrElse(travelPoint::setStationName, () -> travelPoint.setStationName(name));
        travelPoint.setCity(city);
        travelPoint.setCountry(Locale.GERMANY);
        Optional.ofNullable(coordinate).ifPresent(coord -> travelPoint.setGpsCoordinates(mapHvvCoordinatesToBsCoordinates(coord)));
        travelPoint.setVehicleTypes(vehicleTypes);
        return travelPoint;
    }

    private static Coordinates mapHvvCoordinatesToBsCoordinates(Coordinate coordinate) {
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLatitude(coordinate.getY());
        coordinates.setLongitude(coordinate.getX());
        return coordinates.build();
    }

    private static Duration generateDurationFromStartToDestination(Date start, Date destination) {
        return Duration.between(LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(destination.toInstant(), ZoneId.systemDefault()));
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
        BeanUtils.copyProperties(travelPointResponse.getResults().get(FIRST), hvvStation);
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
            return Map.of(UUID.randomUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(callStatus.getException()));
        }
    }

    private Journey mapRealtimeScheduleToJourney(RealtimeSchedule realtimeSchedule) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        journey.setStart(buildTravelPointWith(realtimeSchedule.getStart()).build());
        journey.setDestination(buildTravelPointWith(realtimeSchedule.getDest()).build());
        journey.setPrice(buildPriceFrom(realtimeSchedule.getTariffInfos().get(FIRST).getTicketInfos().get(FIRST)));
        journey.setChildPrice(buildPriceFrom(realtimeSchedule.getTariffInfos().get(FIRST).getTicketInfos().get(1)));
        journey.setTravelProvider(TravelProvider.HVV);
        String departureDate = realtimeSchedule.getScheduleElements().get(FIRST).getFrom().getDepTime().getDate();
        String departureTime = realtimeSchedule.getScheduleElements().get(FIRST).getFrom().getDepTime().getTime();
        journey.setStartTime(generateDateFromDateAndTime(departureDate, departureTime));
        int journeyBetweenSize = realtimeSchedule.getScheduleElements().size() - 1;
        String arrivalDate = realtimeSchedule.getScheduleElements().get(journeyBetweenSize).getTo().getArrTime().getDate();
        String arrivalTime = realtimeSchedule.getScheduleElements().get(journeyBetweenSize).getTo().getArrTime().getTime();
        journey.setArrivalTime(generateDateFromDateAndTime(arrivalDate, arrivalTime));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setBetweenTrips(buildTripsBetweenWith(realtimeSchedule.getScheduleElements()));
        return journey.build();
    }

    private List<Journey> buildTripsBetweenWith(List<ScheduleElement> tripsBetween) {
        return tripsBetween
                .stream()
                .map(this::buildBetweenTripJourneyWith)
                .collect(Collectors.toList());
    }

    private Journey buildBetweenTripJourneyWith(ScheduleElement scheduleElement) {
        Journey.JourneyBuilder betweenTrip = new Journey.JourneyBuilder();
        betweenTrip.setId(uuidService.createUUID());
        betweenTrip.setStart(buildTravelPointWith(scheduleElement.getFrom()).build());
        betweenTrip.setDestination(buildTravelPointWith(scheduleElement.getTo()).build());
        betweenTrip.setStartTime(generateDateFromDateAndTime(
                scheduleElement.getFrom().getDepTime().getDate(),
                scheduleElement.getFrom().getDepTime().getTime())
        );
        betweenTrip.setArrivalTime(generateDateFromDateAndTime(
                scheduleElement.getTo().getArrTime().getDate(),
                scheduleElement.getTo().getArrTime().getTime())
        );
        betweenTrip.setDuration(generateDurationFromStartToDestination(betweenTrip.getStartTime(), betweenTrip.getArrivalTime()));
        betweenTrip.setProviderId(scheduleElement.getLine().getId());
        betweenTrip.setVehicleName(scheduleElement.getLine().getType().getLongInfo());
        betweenTrip.setVehicleType(scheduleElement.getLine().getType().getSimpleType());
        betweenTrip.setVehicleNumber(scheduleElement.getLine().getName());
        betweenTrip.setTravelLine(buildTravelLineWith(scheduleElement));
        return betweenTrip.build();
    }

    private List<TravelPoint> mapHvvStationListToTravelPointList(HvvStationList hvvStationList) {
        return hvvStationList.getStations()
                .stream()
                .map(station -> buildTravelPointWith(station.getId(), station.getCombinedName(), station.getName(), station.getCity(), station.getCoordinate(), station.getVehicleTypes()).build())
                .collect(Collectors.toList());
    }
}