package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.TimeConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.blablacar.Place;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import de.blackforestsolutions.generatedcontent.blablacar.Trip;
import de.blackforestsolutions.generatedcontent.blablacar.Waypoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;


@Slf4j
@Service
public class BlaBlaCarMapperServiceImpl implements BlaBlaCarMapperService {

    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final double METRES_TO_KILOMETRES_COEFFICIENT = 1000.0d;

    private final UuidService uuidService;

    @Autowired
    public BlaBlaCarMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    @Override
    public Flux<CallStatus<Journey>> buildJourneysWith(Rides rides) {
        return Mono.just(rides)
                .flatMapMany(ride -> Flux.fromIterable(ride.getTrips()))
                .flatMap(this::buildJourneyWith);
    }

    private Mono<CallStatus<Journey>> buildJourneyWith(Trip trip) {
        try {
            return Mono.just(new CallStatus<>(new Journey.JourneyBuilder(uuidService.createUUID())
                    .setLegs(buildLegsWith(trip))
                    .build(), Status.SUCCESS, null)
            );
        } catch (Exception e) {
            return Mono.just(new CallStatus<>(null, Status.FAILED, e));
        }
    }

    private LinkedHashMap<UUID, Leg> buildLegsWith(Trip trip) {
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        Leg leg = buildLegWith(trip);
        legs.put(leg.getId(), leg);
        return legs;
    }

    private Leg buildLegWith(Trip trip) {
        List<Waypoint> wayPoints = trip.getWaypoints();
        ZonedDateTime startTime = LocalDateTime.ofInstant(Instant.parse(wayPoints.get(FIRST_INDEX).getDateTime()), ZoneId.ofOffset("GMT", ZoneOffset.ofHours(0))).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
        ZonedDateTime arrivalTime = LocalDateTime.ofInstant(Instant.parse(wayPoints.get(wayPoints.size() - SECOND_INDEX).getDateTime()), ZoneId.ofOffset("GMT", ZoneOffset.ofHours(0))).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
        return new Leg.LegBuilder(uuidService.createUUID())
                .setStartTime(startTime)
                .setArrivalTime(arrivalTime)
                .setDuration(Duration.between(startTime, arrivalTime))
                .setStart(buildTravelPointWith(wayPoints.get(FIRST_INDEX).getPlace()))
                .setDestination(buildTravelPointWith(wayPoints.get(wayPoints.size() - SECOND_INDEX).getPlace()))
                .setPrice(buildPriceWith(trip))
                .setHasPrice(true)
                .setDistance(new Distance(trip.getDistanceInMeters() / METRES_TO_KILOMETRES_COEFFICIENT, Metrics.KILOMETERS))
                .setVehicleType(VehicleType.CAR)
                .setVehicleName(Optional.ofNullable(trip.getVehicle()).map(vehicle -> vehicle.getMake().concat(" ").concat(vehicle.getModel())).orElse(""))
                .build();
    }

    private TravelPoint buildTravelPointWith(Place place) {
        return new TravelPoint.TravelPointBuilder()
                .setCity(place.getCity())
                .setStationName(place.getAddress())
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(place.getLatitude(), place.getLongitude()).build())
                .setCountry(Locale.forLanguageTag(place.getCountryCode()))
                .build();
    }

    private Price buildPriceWith(Trip trip) {
        return new Price.PriceBuilder()
                .setValues(Map.of(PriceCategory.ADULT, new BigDecimal(trip.getPrice().getAmount())))
                .setAffiliateLinks(Map.of(PriceCategory.ADULT, trip.getLink()))
                .setCurrency(Currency.getInstance(String.valueOf(trip.getPrice().getCurrency())))
                .setSymbol("€")
                .build();
    }
}
