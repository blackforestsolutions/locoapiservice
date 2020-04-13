package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class RMVMapperServiceImpl implements RMVMapperService {

    private static final int START_INDEX = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(RMVMapperServiceImpl.class);
    private static final int INDEX_SUBTRACTION = 1;
    private final UuidService uuidService;
    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;

    @Autowired
    public RMVMapperServiceImpl(UuidService uuidService) {
        this.uuidService = uuidService;
    }

    @Override
    public CallStatus getIdFrom(String resultBody) {
        StringReader readerResultBody = new StringReader(resultBody);
        LocationList locationList;
        try {
            jaxbContext = JAXBContext.newInstance(LocationList.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            locationList = (LocationList) unmarshaller.unmarshal(readerResultBody);
        } catch (JAXBException e) {
            log.error("Error during unmarshalling of XML Objects: {}", readerResultBody, e);
            return new CallStatus(null, Status.FAILED, e);
        }
        StopLocation stopLocation = (StopLocation) locationList.getStopLocationOrCoordLocation().get(0);
        return new CallStatus(stopLocation.getId(), Status.SUCCESS, null);
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysFrom(String resultBody) {
        TripList tripList;
        StringReader readerResultBody = new StringReader(resultBody);
        try {
            jaxbContext = JAXBContext.newInstance(TripList.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            tripList = (TripList) unmarshaller.unmarshal(readerResultBody);
        } catch (JAXBException e) {
            log.error("Error during unmarshalling of XML Objects: {}", readerResultBody, e);
            return Collections.singletonMap(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(e));
        }
        return getJourneysFrom(tripList);
    }

    private Map<UUID, JourneyStatus> getJourneysFrom(TripList tripList) {
        return tripList.getTrip()
                .stream()
                .map(this::getJourneyFrom)
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journey -> journey));
    }

    private Journey getJourneyFrom(Trip trip) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        Map<Integer, TravelPoint> holds = extractFrom(trip);
        journey.setTravelLine(buildTravelLine(holds));
        journey.setArrivalTime(journey.getTravelLine().getBetweenHolds().get(holds.size() - INDEX_SUBTRACTION).getDepartureTime());
        journey.setStartTime(holds.get(START_INDEX).getDepartureTime());
        journey.setStart(holds.get(START_INDEX));
        journey.setDestination(journey.getTravelLine().getBetweenHolds().get(holds.size() - INDEX_SUBTRACTION));
        journey.setDuration(Duration.ofMinutes(trip.getDuration().getMinutes()));
        journey.setTravelProvider(TravelProvider.DB);
        journey.setPrice(extractPriceFrom(trip));
        return journey.build();
    }

    private TravelLine buildTravelLine(Map<Integer, TravelPoint> holds) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setBetweenHolds(holds);
        return travelLine.build();
    }

    private Price extractPriceFrom(Trip trip) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        if (trip.getTariffResult() != null) {
            price.setValue(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getPrice());
            price.setCurrency(Currency.getInstance(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getCur()));
            price.setSymbol(price.getCurrency().getSymbol());
        }
        return price.build();
    }

    private Map<Integer, TravelPoint> extractFrom(Trip trip) {
        AtomicInteger counter = new AtomicInteger(0);
        Stream<TravelPoint> origin = trip.getLegList().getLeg()
                .stream()
                .map(leg -> buildTravelPointWith(leg, leg.getOrigin()));
        Stream<TravelPoint> destination = trip.getLegList().getLeg()
                .stream()
                .skip(trip.getLegList().getLeg().size() - INDEX_SUBTRACTION)
                .map(leg -> buildTravelPointWith(leg, leg.getDestination()));
        return Stream.concat(origin, destination)
                .collect(Collectors.toMap(travelPoint -> counter.getAndIncrement(), travelPoint -> travelPoint));
    }

    private TravelPoint buildTravelPointWith(Leg leg, OriginDestType originDestType) {
        return buildTravelPointWith(
                leg,
                originDestType.getLat().doubleValue(),
                originDestType.getLon().doubleValue(),
                originDestType.getName(),
                originDestType.getExtId(),
                originDestType.getTrack(),
                originDestType.getDate()
        );
    }

    private TravelPoint buildTravelPointWith(Leg leg, double latitude, double longitude, String name, String exitId, String track, String date) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        OriginDestType origin = leg.getOrigin();
        OriginDestType destination = leg.getDestination();
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(latitude, longitude).build());
        travelPoint.setCity(name);
        travelPoint.setStationName(name);
        travelPoint.setStationId(exitId);
        travelPoint.setPlatform(track);
        try {
            travelPoint.setDepartureTime(buildDateFrom(date.concat("-").concat(origin.getTime())));
            travelPoint.setArrivalTime(buildDateFrom(destination.getDate().concat("-").concat(destination.getTime())));
        } catch (ParseException e) {
            LOGGER.error("Error during date parsing", e);
        }
        return travelPoint.build();
    }

    private Date buildDateFrom(String dateTime) throws ParseException {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss");
        return inFormat.parse(dateTime);
    }
}
