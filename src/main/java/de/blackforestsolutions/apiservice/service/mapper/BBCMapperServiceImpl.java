package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.bbc.Rides;
import de.blackforestsolutions.generatedcontent.bbc.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BBCMapperServiceImpl implements BBCMapperService {

    private final UuidService uuidService;

    @Autowired
    public BBCMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    private static CallStatus retrieveRidesStatusFrom(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return new CallStatus(mapper.readValue(jsonString, Rides.class), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error while processing json: ", e);
            return new CallStatus(null, Status.FAILED, e);
        }
    }

    private static TravelPoint buildTravelPointWith(Trip trip, boolean departure) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        if (isSafeToUnwrap(trip, departure) && departure) {
            travelPoint.setCity(String.valueOf(trip.getDeparturePlace().getCityName()));
            travelPoint.setStreet(String.valueOf(trip.getDeparturePlace().getAddress()));
            Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
            coordinates.setLatitude(trip.getDeparturePlace().getLatitude());
            coordinates.setLongitude(trip.getDeparturePlace().getLongitude());
            travelPoint.setGpsCoordinates(coordinates.build());
            Locale localeVar = new Locale(trip.getDeparturePlace().getCountryCode());
            travelPoint.setCountry(localeVar);

        }
        if (isSafeToUnwrap(trip, departure) && !departure) {
            travelPoint.setCity(String.valueOf(trip.getArrivalPlace().getCityName()));
            travelPoint.setStreet(String.valueOf(trip.getArrivalPlace().getAddress()));
            Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
            coordinates.setLatitude(trip.getArrivalPlace().getLatitude());
            coordinates.setLongitude(trip.getArrivalPlace().getLongitude());
            travelPoint.setGpsCoordinates(coordinates.build());
            Locale localeVar = new Locale(trip.getArrivalPlace().getCountryCode());
            travelPoint.setCountry(localeVar);
        }
        return travelPoint.build();
    }

    private static boolean isSafeToUnwrap(Trip trip, boolean departure) {
        if (departure) {
            return isSafeToUnwrapDeparture(trip);
        }
        return isSafeToUnwrapArrival(trip);
    }

    private static boolean isSafeToUnwrapArrival(Trip trip) {
        return trip.getArrivalPlace() != null
                && trip.getArrivalPlace().getCityName() != null
                && trip.getArrivalPlace().getAddress() != null
                && trip.getArrivalPlace().getCountryCode() != null;
    }

    private static boolean isSafeToUnwrapDeparture(Trip trip) {
        return trip.getDeparturePlace() != null
                && trip.getDeparturePlace().getCityName() != null
                && trip.getDeparturePlace().getAddress() != null
                && trip.getDeparturePlace().getCountryCode() != null;
    }

    private static Price buildPriceWith(Trip trip) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        EnumMap<PriceCategory, BigDecimal> values = new EnumMap<>(PriceCategory.class);
        // todo price is int value in generated content
        values.put(PriceCategory.ADULT_REDUCED, new BigDecimal(trip.getPrice().getValue()));
        values.put(PriceCategory.ADULT, new BigDecimal(trip.getPriceWithCommission().getValue()));
        price.setValues(values);
        price.setCurrency(Currency.getInstance(String.valueOf(trip.getPrice().getCurrency())));
        price.setSymbol(trip.getPrice().getSymbol());
        return price.build();
    }

    private static Distance buildDistanceWith(Trip trip) {
        Distance distance = null;
        if (trip.getDistance() != null) {
            distance = new Distance(trip.getDistance().getValue());
        }
        return distance;
    }

    private static Date buildDateFrom(Trip trip) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateStringFixed = trip.getDepartureDate().replace("/", "-");
            return simpleDateFormat.parse(dateStringFixed);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date()", e);
            return new Date();
        }
    }

    @Override
    public Map<UUID, JourneyStatus> map(String jsonString) {
        return mapTripsToJourney(retrieveRidesStatusFrom(jsonString));
    }

    private Map<UUID, JourneyStatus> mapTripsToJourney(CallStatus callStatus) {
        if (callStatus.getCalledObject() != null) {
            Rides ridesStatus = (Rides) callStatus.getCalledObject();
            return ridesStatus
                    .getTrips()
                    .stream()
                    .map(this::buildJourneyWith)
                    .collect(Collectors.toMap(Journey::getId, JourneyStatusBuilder::createJourneyStatusWith));
        } else {
            UUID errorUuid = UUID.randomUUID();
            return Map.of(errorUuid, JourneyStatusBuilder.createJourneyStatusProblemWith(callStatus.getException()));
        }

    }

    private Journey buildJourneyWith(Trip trip) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        Leg leg = buildLegWith(trip);
        legs.put(leg.getId(), leg);
        journey.setLegs(legs);
        return journey.build();
    }

    private Leg buildLegWith(Trip trip) {
        Leg.LegBuilder leg = new Leg.LegBuilder(uuidService.createUUID());
        leg.setStartTime(buildDateFrom(trip));
        leg.setStart(buildTravelPointWith(trip, true));
        leg.setDestination(buildTravelPointWith(trip, false));
        leg.setPrice(buildPriceWith(trip));
        leg.setDistance(buildDistanceWith(trip));
        leg.setProviderId(trip.getPermanentId());
        leg.setVehicleNumber(trip.getCar().getId());
        leg.setVehicleName(trip.getCar().getMake().concat(trip.getCar().getModel()));
        return leg.build();
    }
}
