package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.*;
import de.blackforestsolutions.generatedcontent.hafas.response.locations.HafasLocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.util.CoordinatesUtil.convertWGS84ToCoordinatesWith;

@Slf4j
@Service
public class HafasMapperServiceImpl implements HafasMapperService {

    private static final int FIRST_INDEX = 0;
    private static final int TIME_LENGTH = 8;
    private static final int INDEX_SUBTRACTION = 1;
    private static final String JOURNEY = "JNY";
    private static final String TELE_TAXI = "TETA";
    private static final String WALK = "WALK";
    private static final String TRANSFER = "TRSF";

    private final UuidService uuidService;

    private List<LocL> locations;
    private List<ProdL> vehicles;
    private String date;

    @Autowired
    public HafasMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    private static Duration generateDurationBetween(Date start, Date destination) {
        return Duration.between(
                LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(destination.toInstant(), ZoneId.systemDefault())
        );
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysFrom(String body, TravelProvider travelProvider, HafasPriceMapper priceMapper) {
        ObjectMapper mapper = new ObjectMapper();
        HafasJourneyResponse response;
        try {
            response = mapper.readValue(body, HafasJourneyResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Error while mapping hafas journey json: ", e);
            return Collections.singletonMap(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(e));
        }
        return getJourneysFrom(response, travelProvider, priceMapper);
    }

    @Override
    public CallStatus getIdFrom(String resultBody) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        HafasLocationResponse response;
        try {
            response = mapper.readValue(resultBody, HafasLocationResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Errow while mapping travelPoint json: ", e);
            return new CallStatus(null, Status.FAILED, e);
        }
        LocL location = response.getSvcResL().get(FIRST_INDEX).getRes().getMatch().getLocL().get(FIRST_INDEX);
        return new CallStatus(selectIdFrom(location), Status.SUCCESS, null);
    }

    private String selectIdFrom(LocL location) {
        return Optional.ofNullable(location.getExtId())
                .orElseGet(() -> StringUtils.substringBetween(location.getLid(), "@L=", "@B"));
    }

    private Map<UUID, JourneyStatus> getJourneysFrom(HafasJourneyResponse journeyResponse, TravelProvider travelProvider, HafasPriceMapper priceMapper) {
        Res response = journeyResponse.getSvcResL().get(FIRST_INDEX).getRes();
        locations = response.getCommon().getLocL();
        vehicles = response.getCommon().getProdL();
        return response
                .getOutConL()
                .stream()
                .map(journey -> getJourneyFrom(journey, travelProvider, priceMapper))
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journeyStatus -> journeyStatus));
    }

    private Journey getJourneyFrom(OutConL hafasJourney, TravelProvider travelProvider, HafasPriceMapper priceMapper) {
        date = hafasJourney.getDate();
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        Dep departure = hafasJourney.getDep();
        journey.setStart(buildTravelPointWith(locations.get(departure.getLocX()), null, null, departure.getDPlatfS()));
        journey.setStartTime(buildDateWith(hafasJourney.getDate(), departure.getDTimeS()));
        Arr arrival = hafasJourney.getArr();
        journey.setDestination(buildTravelPointWith(locations.get(arrival.getLocX()), null, null, arrival.getAPlatfS()));
        journey.setArrivalTime(buildDateWith(hafasJourney.getDate(), arrival.getATimeS()));
        journey.setDuration(generateDurationBetween(journey.getStartTime(), journey.getArrivalTime()));
        Optional.ofNullable(arrival.getATimeR()).ifPresent(prognosedArrivalTime -> journey.setDelay(generateDurationBetween(journey.getArrivalTime(), buildDateWith(hafasJourney.getDate(), prognosedArrivalTime))));
        journey.setPrice(priceMapper.map(hafasJourney.getTrfRes()));
        if (hafasJourney.getSecL().size() != 1) {
            journey.setBetweenTrips(buildBetweenTripsWith(hafasJourney.getSecL()));
        } else {
            SecL leg = hafasJourney.getSecL().get(FIRST_INDEX);
            setJourneyPropertiesByTransportType(leg, journey);
        }
        journey.setTravelProvider(travelProvider);
        return journey.build();
    }

    private List<Journey> buildBetweenTripsWith(List<SecL> betweenTrips) {
        return betweenTrips
                .stream()
                .map(this::getJourneyFrom)
                .collect(Collectors.toList());
    }

    private Journey getJourneyFrom(SecL betweenTrip) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        Dep departure = betweenTrip.getDep();
        journey.setStart(buildTravelPointWith(locations.get(departure.getLocX()), null, null, departure.getDPlatfS()));
        Arr arrival = betweenTrip.getArr();
        journey.setDestination(buildTravelPointWith(locations.get(betweenTrip.getArr().getLocX()), null, null, arrival.getAPlatfS()));
        journey.setStartTime(buildDateWith(date, departure.getDTimeS()));
        journey.setArrivalTime(buildDateWith(date, arrival.getATimeS()));
        Optional.ofNullable(betweenTrip.getArr().getATimeR()).ifPresent(prognosedArrivalTime -> journey.setDelay(generateDurationBetween(journey.getArrivalTime(), buildDateWith(date, prognosedArrivalTime))));
        setJourneyPropertiesByTransportType(betweenTrip, journey);
        return journey.build();
    }

    private void setJourneyPropertiesByTransportType(SecL betweenTrip, Journey.JourneyBuilder journey) {
        if (betweenTrip.getType().equals(WALK) || betweenTrip.getType().equals(TRANSFER)) {
            journey.setDistance(new Distance(betweenTrip.getGis().getDist()));
            journey.setVehicleType(WALK);
        } else if (betweenTrip.getType().equals(JOURNEY) || betweenTrip.getType().equals(TELE_TAXI)) {
            Jny hafasLeg = betweenTrip.getJny();
            journey.setProviderId(hafasLeg.getJid());
            journey.setTravelLine(buildTravelLineWith(hafasLeg));
            ProdL vehicle = vehicles.get(hafasLeg.getProdX());
            journey.setVehicleType(vehicle.getProdCtx().getCatOut());
            journey.setVehicleName(vehicle.getProdCtx().getCatOutL());
            journey.setVehicleNumber(vehicle.getName());
        } else {
            log.info("No valid type for leg found in: ".concat(HafasMapperService.class.getName()));
        }
    }

    private TravelLine buildTravelLineWith(Jny betweenHolds) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        if (!betweenHolds.getStopL().isEmpty()) {
            travelLine.setDirection(buildTravelPointWith(betweenHolds.getDirTxt()));
            travelLine.setBetweenHolds(buildBetweenHoldsWith(betweenHolds.getStopL()));
            return travelLine.build();
        }
        return travelLine.build();
    }

    private Map<Integer, TravelPoint> buildBetweenHoldsWith(List<StopL> intermediateStops) {
        AtomicInteger counter = new AtomicInteger(0);
        return intermediateStops
                .stream()
                .skip(INDEX_SUBTRACTION)
                .limit(intermediateStops.size() - INDEX_SUBTRACTION - INDEX_SUBTRACTION)
                .map(stop -> buildTravelPointWith(locations.get(stop.getLocX()), stop.getDTimeS(), stop.getATimeS(), extractPlatformFrom(stop)))
                .collect(Collectors.toMap(travelPoint -> counter.getAndIncrement(), travelPoint -> travelPoint));
    }

    private String extractPlatformFrom(StopL intermediateStop) {
        if (Optional.ofNullable(intermediateStop.getAPlatfR()).isPresent()) {
            return intermediateStop.getAPlatfR();
        }
        if (Optional.ofNullable(intermediateStop.getDPlatfR()).isPresent()) {
            return intermediateStop.getDPlatfR();
        }
        if (Optional.ofNullable(intermediateStop.getAPlatfS()).isPresent()) {
            return intermediateStop.getAPlatfS();
        }
        if (Optional.ofNullable(intermediateStop.getDPlatfS()).isPresent()) {
            return intermediateStop.getAPlatfS();
        }
        return "";
    }

    private TravelPoint buildTravelPointWith(LocL location, String departureTime, String arrivalTime, String platform) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName(location.getName());
        travelPoint.setStationId(selectIdFrom(location));
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(location.getCrd().getX(), location.getCrd().getY()));
        Optional.ofNullable(departureTime).ifPresent(departure -> travelPoint.setDepartureTime(buildDateWith(date, departure)));
        Optional.ofNullable(arrivalTime).ifPresent(arrival -> travelPoint.setArrivalTime(buildDateWith(date, arrival)));
        Optional.ofNullable(platform).ifPresent(travelPoint::setPlatform);
        return travelPoint.build();
    }

    private TravelPoint buildTravelPointWith(String location) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        return Optional.ofNullable(location)
                .map(loc -> {
                    travelPoint.setStationName(loc);
                    return travelPoint.build();
                })
                .orElseGet(() -> {
                    log.info("Travelpoint was not found and replaced by empty travelpoint");
                    return travelPoint.build();
                });
    }

    private Date buildDateWith(String date, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate datePart = LocalDate.parse(date, dateFormatter);
        if (time.length() == TIME_LENGTH) {
            time = StringUtils.substring(time, 2);
            datePart = datePart.plusDays(1);
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        LocalTime timePart = LocalTime.parse(time, timeFormatter);
        LocalDateTime dateTime = LocalDateTime.of(datePart, timePart);
        return convertToDate(dateTime);
    }

    private Date convertToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
