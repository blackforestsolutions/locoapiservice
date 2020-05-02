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
import static org.apache.commons.lang.StringUtils.deleteWhitespace;

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

    private static final String BUS = "Bus";
    private static final String REGIONAL_TRAIN_RE = "RE";
    private static final String REGIONAL_TRAIN_IC = "IC";
    private static final String REGIONAL_TRAIN_R = "R";
    private static final String REGIONAL_TRAIN_RB = "RB";
    private static final String TRAIN_ICE = "ICE";
    private static final String TRAIN_CJX = "CJX";
    private static final String TRAIN_RJX = "RJX";
    private static final String TAXI = "AST";
    private static final String SBAHN_S = "S";
    private static final String SBAHN_STR = "STR";
    private static final String UBAHN = "U";
    private static final String TRAM = "RT";

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
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        journey.setLegs(getLegsFrom(hafasJourney.getSecL(), travelProvider, priceMapper.map(hafasJourney.getTrfRes())));
        return journey.build();
    }

    private LinkedHashMap<UUID, Leg> getLegsFrom(List<SecL> betweenTrips, TravelProvider travelProvider, Price price) {
        AtomicInteger counter = new AtomicInteger(0);
        return betweenTrips
                .stream()
                .map(secL -> getLegFrom(secL, travelProvider, price, counter.getAndIncrement()))
                .collect(Collectors.toMap(leg -> leg.getId(), leg -> leg, (prev, next) -> next, LinkedHashMap::new));
    }

    private Leg getLegFrom(SecL betweenTrip, TravelProvider travelProvider, Price price, int index) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setStart(buildTravelPointWith(locations.get(betweenTrip.getDep().getLocX()), null, null, betweenTrip.getDep().getDPlatfS()));
        leg.setDestination(buildTravelPointWith(locations.get(betweenTrip.getArr().getLocX()), null, null, betweenTrip.getArr().getAPlatfS()));
        leg.setStartTime(buildDateWith(date, betweenTrip.getDep().getDTimeS()));
        leg.setArrivalTime(buildDateWith(date, betweenTrip.getArr().getATimeS()));
        leg.setDuration(generateDurationBetween(leg.getStartTime(), leg.getArrivalTime()));
        Optional.ofNullable(betweenTrip.getArr().getATimeR()).ifPresent(prognosedArrivalTime -> leg.setDelay(generateDurationBetween(leg.getArrivalTime(), buildDateWith(date, prognosedArrivalTime))));
        if (betweenTrip.getType().equals(WALK) || betweenTrip.getType().equals(TRANSFER)) {
            leg.setDistance(new Distance(betweenTrip.getGis().getDist()));
            leg.setVehicleType(VehicleType.WALK);
        } else if (betweenTrip.getType().equals(JOURNEY) || betweenTrip.getType().equals(TELE_TAXI)) {
            Jny hafasLeg = betweenTrip.getJny();
            leg.setProviderId(hafasLeg.getJid());
            leg.setTravelProvider(travelProvider);
            leg.setTravelLine(buildTravelLineWith(hafasLeg));
            ProdL vehicle = vehicles.get(hafasLeg.getProdX());
            leg.setVehicleType(getVehicleType(vehicle.getProdCtx().getCatOut()));
            leg.setVehicleName(vehicle.getProdCtx().getCatOutL());
            leg.setVehicleNumber(vehicle.getName());
        } else {
            log.info("No valid type for leg found in: ".concat(HafasMapperService.class.getName()));
        }
        if (index == 0) {
            leg.setPrice(price);
        }
        return leg.build();
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

    private VehicleType getVehicleType(String vehicleType) {
        return switch (deleteWhitespace(vehicleType)) {
            case BUS -> VehicleType.BUS;
            case REGIONAL_TRAIN_RE, REGIONAL_TRAIN_IC, REGIONAL_TRAIN_R, REGIONAL_TRAIN_RB,
                    TRAIN_ICE, TRAIN_CJX, TRAIN_RJX, SBAHN_S, SBAHN_STR, UBAHN, TRAM -> VehicleType.TRAIN;
            case TAXI -> VehicleType.CAR;
            default -> null;
        };
    }

}
