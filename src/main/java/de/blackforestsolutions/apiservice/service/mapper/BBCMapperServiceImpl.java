package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.bbc.Duration;
import de.blackforestsolutions.generatedcontent.bbc.Place;
import de.blackforestsolutions.generatedcontent.bbc.Rides;
import de.blackforestsolutions.generatedcontent.bbc.Trip;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder.createJourneyStatusProblemWith;
import static de.blackforestsolutions.apiservice.service.mapper.MapperService.generateDurationFromStartToDestination;

@Slf4j
@Service
public class BBCMapperServiceImpl implements BBCMapperService {

    private static final String SECONDS = "s";
    private static final int FIRST = 1;
    private static final int TRIP_PLAN_MIN_SIZE = 2;

    private final UuidService uuidService;

    @Autowired
    public BBCMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    @Override
    public Map<UUID, JourneyStatus> mapJsonToJourneys(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Rides rides = mapper.readValue(jsonString, Rides.class);
        return buildJourneysWith(rides);
    }

    private Map<UUID, JourneyStatus> buildJourneysWith(Rides rides) {
        return rides
                .getTrips()
                .stream()
                .map(this::buildJourneyWith)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<UUID, JourneyStatus> buildJourneyWith(Trip trip) {
        try {
            LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
            Leg leg = buildLegWith(trip);
            legs.put(leg.getId(), leg);
            UUID id = uuidService.createUUID();
            return Map.entry(id, JourneyStatusBuilder.createJourneyStatusWith(new Journey.JourneyBuilder(id)
                    .setLegs(legs)
                    .build())
            );
        } catch (Exception e) {
            log.error("Unable to map Pojo: ", e);
            return Map.entry(uuidService.createUUID(), createJourneyStatusProblemWith(List.of(e), Collections.emptyList()));
        }
    }

    private Leg buildLegWith(Trip trip) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setStartTime(buildDateFrom(trip.getDepartureDate()));
        leg.setArrivalTime(buildArrivalTimeWith(leg.getStartTime(), trip.getDuration()));
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
        leg.setStart(buildTravelPointWith(trip.getDeparturePlace()));
        leg.setDestination(buildTravelPointWith(trip.getArrivalPlace()));
        leg.setPrice(buildPriceWith(trip));
        leg.setDistance(new Distance(trip.getDistance().getValue(), Metrics.KILOMETERS));
        leg.setProviderId(trip.getMainPermanentId());
        if (StringUtils.isNotEmpty(trip.getComment())) {
            leg.setIncidents(List.of(trip.getComment()));
        }
        if (trip.getTripPlan().size() > TRIP_PLAN_MIN_SIZE) {
            leg.setTravelLine(buildTravelLine(trip.getTripPlan()));
        }
        leg.setVehicleType(VehicleType.CAR);
        Optional.ofNullable(trip.getCar()).ifPresent(car -> leg.setVehicleName(car.getMake().concat(" ").concat(car.getModel())));
        return leg.build();
    }

    private TravelPoint buildTravelPointWith(Place place) {
        return new TravelPoint.TravelPointBuilder()
                .setCity(place.getCityName())
                .setStationName(place.getAddress())
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(place.getLatitude(), place.getLongitude()).build())
                .setCountry(Locale.forLanguageTag(place.getCountryCode()))
                .build();
    }

    private Price buildPriceWith(Trip trip) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal(trip.getPrice().getValue())));
        price.setAffiliateLinks(Map.of(PriceCategory.ADULT, trip.getLinks().getFront()));
        price.setCurrency(Currency.getInstance(String.valueOf(trip.getPrice().getCurrency())));
        price.setSymbol(trip.getPrice().getSymbol());
        return price.build();
    }

    private TravelLine buildTravelLine(List<Place> tripPlans) {
        return new TravelLine.TravelLineBuilder()
                .setBetweenHolds(buildBetweenHoldsWith(tripPlans))
                .build();
    }

    private Map<Integer, TravelPoint> buildBetweenHoldsWith(List<Place> tripPlans) {
        AtomicInteger counter = new AtomicInteger(0);
        return tripPlans
                .stream()
                .skip(FIRST)
                .limit(tripPlans.size() - FIRST - FIRST)
                .map(this::buildTravelPointWith)
                .collect(Collectors.toMap(betweenHold -> counter.getAndIncrement(), beweenHold -> beweenHold));
    }

    private Date buildDateFrom(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date()", e);
            return new Date();
        }
    }

    private Date buildArrivalTimeWith(Date departureTime, Duration duration) {
        if (!duration.getUnity().equals(SECONDS)) {
            log.error("No unity found for building arrival time!");
            return new Date();
        }
        return DateUtils.addSeconds(departureTime, duration.getValue());
    }
}
