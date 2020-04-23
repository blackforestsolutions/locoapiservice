package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Leg;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.generateDurationFromStartToDestination;

@Slf4j
@Service
public class RMVMapperServiceImpl implements RMVMapperService {

    private static final int START_INDEX = 0;
    private static final int SECOND_INDEX = 1;

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
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        journey.setLegs(getLegsFrom(trip.getLegList()));
        journey.setPrice(extractPriceFrom(trip));
        return journey.build();
    }

    private LinkedHashMap<UUID, Leg> getLegsFrom(LegList legList) {
        return legList.getLeg()
                .stream()
                .map(this::getLegFrom)
                .collect(Collectors.toMap(leg -> leg.getId(), leg -> leg, (prev, next) -> next, LinkedHashMap::new));
    }

    private Leg getLegFrom(de.blackforestsolutions.generatedcontent.rmv.hafas_rest.Leg leg) {
        Leg.LegBuilder newLeg = new Leg.LegBuilder(uuidService.createUUID());
        newLeg.setStart(buildTravelPointWith(leg.getOrigin()));
        newLeg.setDestination(buildTravelPointWith(leg.getDestination()));
        newLeg.setStartTime(buildDateFrom(leg.getOrigin().getDate().concat("-").concat(leg.getOrigin().getTime())));
        newLeg.setArrivalTime(buildDateFrom(leg.getDestination().getDate().concat("-").concat(leg.getDestination().getTime())));
        newLeg.setDuration(generateDurationFromStartToDestination(newLeg.getStartTime(), newLeg.getArrivalTime()));
        newLeg.setTravelProvider(TravelProvider.RMV);
        newLeg.setVehicleName(leg.getProduct().getName());
        newLeg.setTravelLine(buildTravelLine(leg));
        return newLeg.build();
    }

    private TravelLine buildTravelLine(de.blackforestsolutions.generatedcontent.rmv.hafas_rest.Leg leg) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(buildTravelPointWith(leg.getDirection()));
        AtomicInteger counter = new AtomicInteger(0);
        travelLine.setBetweenHolds(
                Stream.of(leg.getStops().getStop())
                        .flatMap(stopTypes -> stopTypes.stream())
                        .map(this::buildTravelPointWith)
                        .collect(Collectors.toMap(travelPoint -> counter.getAndIncrement(), travelPoint -> travelPoint, (prev, next) -> next, HashMap::new))
        );
        return travelLine.build();
    }

    private Price extractPriceFrom(Trip trip) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        if (trip.getTariffResult() != null) {
            EnumMap<PriceCategory, BigDecimal> values = new EnumMap<>(PriceCategory.class);
            values.put(PriceCategory.ADULT, new BigDecimal(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getPrice()));
            values.put(PriceCategory.CHILD, new BigDecimal(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(SECOND_INDEX).getPrice()));
            price.setValues(values);
            price.setCurrency(Currency.getInstance(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getCur()));
            price.setSymbol(price.getCurrency().getSymbol());
        }
        return price.build();
    }

    private TravelPoint buildTravelPointWith(String direction) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName(direction);
        return travelPoint.build();
    }

    private TravelPoint buildTravelPointWith(StopType stop) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(stop.getLat().doubleValue(), stop.getLon().doubleValue()).build());
        travelPoint.setStationName(stop.getName());
        travelPoint.setStationId(stop.getExtId());
        travelPoint.setDepartureTime(buildDateFrom(stop.getDepDate().concat("-").concat(stop.getDepTime())));
        travelPoint.setArrivalTime(buildDateFrom(stop.getArrDate().concat("-").concat(stop.getArrTime())));
        return travelPoint.build();
    }

    private TravelPoint buildTravelPointWith(OriginDestType originDestType) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(originDestType.getLat().doubleValue(), originDestType.getLon().doubleValue()).build());
        travelPoint.setStationName(originDestType.getName());
        travelPoint.setStationId(originDestType.getExtId());
        travelPoint.setPlatform(originDestType.getTrack());
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    private Date buildDateFrom(String dateTime) {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss");
            return inFormat.parse(dateTime);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date(): ", e);
            return new Date();
        }
    }
}
