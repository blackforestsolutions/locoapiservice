package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.util.CoordinatesUtil;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.searchCh.Leg;
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

    private final UuidService uuidService;

    @Autowired
    public SearchChMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
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

    private static void buildTravelProviderWith(Leg betweenTrip, de.blackforestsolutions.datamodel.Leg.LegBuilder leg) {
        Optional.ofNullable(betweenTrip.getOperator())
                .map(operator -> StringUtils.upperCase(operator).replaceAll("-", "_").replaceAll(" ", ""))
                .map(operator -> {
                    EnumSet.allOf(TravelProvider.class)
                            .stream()
                            .filter(travelProvider -> travelProvider.name().equals(operator))
                            .findFirst()
                            .ifPresentOrElse(leg::setTravelProvider, () -> leg.setUnknownTravelProvider(betweenTrip.getOperator()));
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

    private Map<UUID, JourneyStatus> mapRouteToJourneyMap(Route route) {
        Objects.requireNonNull(route, "Route is not allowed to be null");
        return Optional.ofNullable(route.getConnections())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildLegWith)
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journey -> journey));
    }

    private Journey buildLegWith(Connection connection) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        journey.setLegs(buildLegsWith(connection.getLegs()));
        return journey.build();
    }

    private LinkedHashMap<UUID, de.blackforestsolutions.datamodel.Leg> buildLegsWith(List<Leg> legs) {
        return legs
                .stream()
                .limit(legs.size() - 1)
                .map(this::buildLegWith)
                .collect(Collectors.toMap(de.blackforestsolutions.datamodel.Leg::getId, leg -> leg, (prev, next) -> next, LinkedHashMap::new));
    }

    private de.blackforestsolutions.datamodel.Leg buildLegWith(Leg betweenTrip) {
        de.blackforestsolutions.datamodel.Leg.LegBuilder leg = new de.blackforestsolutions.datamodel.Leg.LegBuilder(uuidService.createUUID());
        leg.setStart(buildTravelPointWith(betweenTrip));
        leg.setDestination(buildDestinationTravelPointWith(betweenTrip.getExit()));
        leg.setStartTime(buildDateFrom(betweenTrip.getDeparture()));
        leg.setArrivalTime(buildDateFrom(betweenTrip.getExit().getArrival()));
        leg.setDuration(buildDurationBetween(leg.getStartTime(), leg.getArrivalTime()));
        Optional.ofNullable(betweenTrip.getStops()).ifPresent(stops -> leg.setTravelLine(buildTravelLineWith(stops)));
        Optional.ofNullable(betweenTrip.getTripid()).ifPresent(leg::setProviderId);
        buildTravelProviderWith(betweenTrip, leg);
        leg.setVehicleType(getVehicleType(betweenTrip.getType()));
        if (Optional.ofNullable(betweenTrip.getLine()).isPresent()) {
            leg.setVehicleName(betweenTrip.getLine());
        }
        return leg.build();
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
            travelPoint.setStationId(station.getLabel());
            travelPoint.setStreet(station.getLabel());
            return travelPoint.build();
        });
    }

    private VehicleType getVehicleType(String vehicleType) {
        return Arrays.stream(SearchChVehicleType.values())
                .filter(searchChVehicleType -> searchChVehicleType.name().equals(StringUtils.upperCase(vehicleType)))
                .findFirst()
                .map(SearchChVehicleType::getVehicleType)
                .orElse(null);
    }

    private enum SearchChVehicleType {
        STRAIN(VehicleType.TRAIN),
        WALK(VehicleType.WALK),
        TRAM(VehicleType.TRAIN),
        EXPRESS_TRAIN(VehicleType.TRAIN),
        BUS(VehicleType.BUS),
        TRAIN(VehicleType.TRAIN);

        private final VehicleType vehicleType;

        SearchChVehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
        }

        VehicleType getVehicleType() {
            return vehicleType;
        }
    }
}
