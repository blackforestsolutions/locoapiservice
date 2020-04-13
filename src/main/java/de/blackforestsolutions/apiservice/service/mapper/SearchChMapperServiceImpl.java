package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.util.CoordinatesUtil;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.searchCh.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.configuration.LocaleConfiguration.LOCALE_SWITZERLAND;

@Slf4j
@Service
public class SearchChMapperServiceImpl implements SearchChMapperService {

    private static final int START = 0;

    private final UuidService uuidService;

    @Autowired
    public SearchChMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    private static List<Journey> buildBetweenTripsWith(List<Leg> legs) {
        return legs
                .stream()
                .filter(leg -> !leg.isIsaddress())
                .map(SearchChMapperServiceImpl::buildJourneyFrom)
                .collect(Collectors.toList());
    }

    private static Journey buildJourneyFrom(Leg leg) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(UUID.randomUUID());
        journey.setStart(buildTravelPointWith(leg));
        journey.setDestination(buildDestinationTravelPointWith(leg.getExit()));
        journey.setStartTime(buildDateFrom(leg.getDeparture()));
        journey.setArrivalTime(buildDateFrom(leg.getExit().getArrival()));
        journey.setDuration(buildDurationBetween(journey.getStartTime(), journey.getArrivalTime()));
        journey.setTravelLine(buildTravelLineWith(leg.getStops()));
        buildTravelProviderWith(leg, journey);
        journey.setVehicleType(leg.getTypeName());
        if (Optional.ofNullable(leg.getLine()).isPresent()) {
            journey.setVehicleNumber(leg.getLine());
        }
        return journey.build();
    }

    private static TravelLine buildTravelLineWith(List<Stop> stops) {
        AtomicInteger counter = new AtomicInteger(0);
        return Optional.ofNullable(stops)
                .orElse(Collections.emptyList())
                .stream()
                .map(SearchChMapperServiceImpl::buildTravelPointWith)
                .collect(Collectors.collectingAndThen(Collectors.toMap(travelpoint -> counter.getAndIncrement(), travelPoint -> travelPoint), betweenHolds -> {
                    TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
                    travelLine.setBetweenHolds(betweenHolds);
                    return travelLine.build();
                }));
    }

    private static TravelPoint buildTravelPointWith(Stop stop) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId(stop.getStopid());
        travelPoint.setStationName(stop.getName());
        travelPoint.setArrivalTime(buildDateFrom(stop.getArrival()));
        travelPoint.setDepartureTime(buildDateFrom(stop.getDeparture()));
        travelPoint.setGpsCoordinates(buildCoordinatesFrom(stop.getX(), stop.getY()));
        return travelPoint.build();
    }

    private static TravelPoint buildTravelPointWith(Leg leg) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId(leg.getStopid());
        travelPoint.setStationName(leg.getName());
        travelPoint.setGpsCoordinates(buildCoordinatesFrom(leg.getX(), leg.getY()));
        travelPoint.setCountry(LOCALE_SWITZERLAND);
        Optional.ofNullable(leg.getTerminal()).ifPresent(travelPoint::setTerminal);
        Optional.ofNullable(leg.getTrack()).ifPresent(travelPoint::setPlatform);
        return travelPoint.build();
    }

    private static TravelPoint buildDestinationTravelPointWith(Exit exit) {
        return Optional.ofNullable(exit)
                .map(e -> {
                    TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
                    Optional.ofNullable(exit.getStopid()).ifPresent(travelPoint::setStationId);
                    travelPoint.setStationName(exit.getName());
                    travelPoint.setGpsCoordinates(buildCoordinatesFrom(exit.getX(), exit.getY()));
                    travelPoint.setCountry(LOCALE_SWITZERLAND);
                    Optional.ofNullable(exit.getTrack()).ifPresent(travelPoint::setPlatform);
                    return travelPoint.build();
                }).orElseGet(() -> new TravelPoint.TravelPointBuilder().build());
    }

    private static TravelPoint buildTravelPointWith(Connection connection, boolean isStart) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        if (isStart) {
            travelPoint.setStationName(connection.getFrom());
            Optional.ofNullable(connection.getLegs().get(START).getStopid()).ifPresent(travelPoint::setStationId);
            Leg start = connection.getLegs().get(START);
            travelPoint.setGpsCoordinates(buildCoordinatesFrom(start.getX(), start.getY()));
        } else {
            int destinationIndex = connection.getLegs().size() - 1;
            travelPoint.setStationName(connection.getTo());
            Optional.ofNullable(connection.getLegs().get(destinationIndex).getStopid()).ifPresent(travelPoint::setStationId);
            Leg destination = connection.getLegs().get(destinationIndex);
            travelPoint.setGpsCoordinates(buildCoordinatesFrom(destination.getX(), destination.getY()));
        }
        travelPoint.setCountry(LOCALE_SWITZERLAND);
        return travelPoint.build();
    }

    private static void buildTravelProviderWith(Leg leg, Journey.JourneyBuilder journey) {
        Optional.ofNullable(leg.getOperator())
                .map(operator -> StringUtils.upperCase(operator).replaceAll("-", "_").replaceAll(" ", ""))
                .map(operator -> {
                    EnumSet.allOf(TravelProvider.class)
                            .stream()
                            .filter(travelProvider -> travelProvider.name().equals(operator))
                            .findFirst()
                            .ifPresentOrElse(journey::setTravelProvider, () -> journey.setUnknownTravelProvider(leg.getOperator()));
                    return new Object();
                });
    }

    private static Coordinates buildCoordinatesFrom(int x, int y) {
        if (x != 0 && y != 0) {
            return CoordinatesUtil.convertCh1903ToCoordinatesWith(x, y);
        }
        return new Coordinates.CoordinatesBuilder().build();
    }

    private static Date buildDateFrom(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date()", e);
            return new Date();
        }
    }

    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(),
                ZoneId.systemDefault()
        );
    }

    private static Duration buildDurationBetween(Date departure, Date arrival) {
        return Duration.between(
                convertToLocalDateTime(departure),
                convertToLocalDateTime(arrival)
        );
    }

    @Override
    public Map<String, TravelPoint> getTravelPointFrom(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapStationListToTravelPointMap(objectMapper.readValue(
                jsonString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Station.class)
        ));
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysFrom(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Route route;
        try {
            route = mapper.readValue(jsonString, Route.class);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing json: {}", jsonString, e);
            return Collections.singletonMap(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(e));
        }
        return mapRouteToJourneyMap(route);
    }

    private Map<String, TravelPoint> mapStationListToTravelPointMap(List<Station> stations) {
        return stations
                .stream()
                .map(this::buildTravelPointWith)
                .collect(Collectors.toMap(TravelPoint::getStationId, travelPoint -> travelPoint));
    }

    private TravelPoint buildTravelPointWith(Station station) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        return Optional.ofNullable(station.getId()).map(id -> {
            travelPoint.setStationName(station.getLabel());
            travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(Integer.parseInt(station.getX()), Integer.parseInt(station.getY())));
            travelPoint.setStationId(id);
            return travelPoint.build();
        }).orElseGet(() -> {
            travelPoint.setStationId(uuidService.createUUID().toString());
            travelPoint.setStreet(station.getLabel());
            return travelPoint.build();
        });
    }

    private Map<UUID, JourneyStatus> mapRouteToJourneyMap(Route route) {
        Objects.requireNonNull(route, "Route is not allowed to be null");
        return Optional.ofNullable(route.getConnections())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildJourneyFrom)
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journey -> journey));
    }

    private Journey buildJourneyFrom(Connection connection) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        journey.setStart(buildTravelPointWith(connection, true));
        journey.setDestination(buildTravelPointWith(connection, false));
        journey.setStartTime(buildDateFrom(connection.getDeparture()));
        journey.setArrivalTime(buildDateFrom(connection.getArrival()));
        journey.setDuration(buildDurationBetween(journey.getStartTime(), journey.getArrivalTime()));
        journey.setBetweenTrips(buildBetweenTripsWith(connection.getLegs()));
        return journey.build();
    }
}
