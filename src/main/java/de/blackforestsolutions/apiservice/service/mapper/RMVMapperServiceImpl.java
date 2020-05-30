package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
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

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.generateDurationFromStartToDestination;

@Slf4j
@Service
public class RMVMapperServiceImpl implements RMVMapperService {

    private static final int START_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;
    private static final int FOURTH_INDEX = 3;
    private static final String PUBLIC_JOURNEY_KEY = "JNY";
    private static final String CAR_KEY = "KISS";
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
        try {
            StopLocation stopLocation = (StopLocation) locationList.getStopLocationOrCoordLocation().get(START_INDEX);
            return new CallStatus(stopLocation.getId(), Status.SUCCESS, null);
        } catch (ClassCastException e) {
            try {
                CoordLocation coordLocation = (CoordLocation) locationList.getStopLocationOrCoordLocation().get(START_INDEX);
                return new CallStatus(coordLocation.getId(), Status.SUCCESS, null);
            } catch (ClassCastException ex) {
                log.error("Error during mapping xml to station type. Type not found.");
                return new CallStatus(null, Status.FAILED, ex);
            }
        }
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
        journey.setLegs(getLegsFrom(trip.getLegList(), extractPriceFrom(trip)));
        return journey.build();
    }

    private LinkedHashMap<UUID, Leg> getLegsFrom(LegList legList, Price price) {
        AtomicInteger counter = new AtomicInteger(0);
        return legList.getLeg()
                .stream()
                .map(leg -> getLegFrom(leg, price, counter.getAndIncrement()))
                .collect(Collectors.toMap(Leg::getId, leg -> leg, (prev, next) -> next, LinkedHashMap::new));
    }

    private Leg getLegFrom(de.blackforestsolutions.generatedcontent.rmv.hafas_rest.Leg leg, Price price, int index) {
        Leg.LegBuilder newLeg = new Leg.LegBuilder(uuidService.createUUID());
        newLeg.setStart(buildTravelPointWith(leg.getOrigin()));
        newLeg.setDestination(buildTravelPointWith(leg.getDestination()));
        newLeg.setStartTime(buildDateFrom(leg.getOrigin().getDate().concat("-").concat(leg.getOrigin().getTime())));
        newLeg.setArrivalTime(buildDateFrom(leg.getDestination().getDate().concat("-").concat(leg.getDestination().getTime())));
        newLeg.setDuration(generateDurationFromStartToDestination(newLeg.getStartTime(), newLeg.getArrivalTime()));
        newLeg.setTravelProvider(TravelProvider.RMV);
        Optional.ofNullable(leg.getDist()).ifPresent(distance -> newLeg.setDistance(new Distance(distance)));
        Optional.ofNullable(leg.getProduct()).ifPresent(product -> newLeg.setVehicleName(product.getName()));
        if (index == 0) {
            newLeg.setPrice(price);
            newLeg.setHasPrice(true);
        }
        if (leg.getType().equals(PUBLIC_JOURNEY_KEY)) {
            newLeg.setTravelLine(buildTravelLine(leg));
            newLeg.setVehicleName(leg.getProduct().getCatOutL());
            newLeg.setVehicleNumber(StringUtils.deleteWhitespace(leg.getProduct().getName()));
            newLeg.setUnknownTravelProvider(leg.getProduct().getOperator());
            newLeg.setIncidents(buildIncidentsWith(leg.getNotes()));
        } else if (leg.getType().equals(CAR_KEY)) {
            newLeg.setVehicleType(VehicleType.CAR);
        } else {
            newLeg.setVehicleType(VehicleType.WALK);
        }
        return newLeg.build();
    }

    private TravelLine buildTravelLine(de.blackforestsolutions.generatedcontent.rmv.hafas_rest.Leg leg) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(buildTravelPointWith(leg.getDirection()));
        Optional.ofNullable(leg.getStops()).ifPresent(stops -> travelLine.setBetweenHolds(extractBetweenHoldsFrom(stops)));
        return travelLine.build();
    }

    private Map<Integer, TravelPoint> extractBetweenHoldsFrom(Stops stops) {
        AtomicInteger counter = new AtomicInteger(0);
        return stops.getStop()
                .stream()
                .skip(SECOND_INDEX)
                .limit(stops.getStop().size() - SECOND_INDEX - SECOND_INDEX)
                .map(this::buildTravelPointWith)
                .collect(Collectors.toMap(travelPoint -> counter.getAndIncrement(), travelPoint -> travelPoint));
    }

    private Price extractPriceFrom(Trip trip) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        // this try catch block is built like this bc PriceCategory.Child throws a array out of bound exception which has then to be caught
        // but the process still has to be continued in order to map PriceCategory.Adult which then provides the price for adults and children
        try {
            if (trip.getTariffResult() != null) {
                price.setValues(Map.of(
                        PriceCategory.ADULT, new BigDecimal(convertPriceToPriceWithComma(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getPrice())), // todo what if only one price?
                        PriceCategory.CHILD, new BigDecimal(convertPriceToPriceWithComma(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(SECOND_INDEX).getPrice())),
                        PriceCategory.ADULT_REDUCED, new BigDecimal(convertPriceToPriceWithComma(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(THIRD_INDEX).getPrice())),
                        PriceCategory.CHILD_REDUCED, new BigDecimal(convertPriceToPriceWithComma(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(FOURTH_INDEX).getPrice()))
                ));
                price.setCurrency(Currency.getInstance(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getCur()));
                price.setSymbol(price.getCurrency().getSymbol());
            }
            return price.build();
        } catch (IndexOutOfBoundsException ioob) {
            log.error("Array out of bound exception: there is only one price which is valid for adults as well as for children", ioob);
            if (trip.getTariffResult() != null) {
                price.setValues(Map.of(
                        PriceCategory.ADULT, new BigDecimal(convertPriceToPriceWithComma(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getPrice()))
                ));
                price.setCurrency(Currency.getInstance(trip.getTariffResult().getFareSetItem().get(START_INDEX).getFareItem().get(START_INDEX).getTicket().get(START_INDEX).getCur()));
                price.setSymbol(price.getCurrency().getSymbol());
            }
            return price.build();
        }
    }

    private String convertPriceToPriceWithComma(int price) {
        StringBuilder sb = new StringBuilder(String.valueOf(price));
        int length = (int) Math.log10(price) + 1;
        sb.insert(length - 2, ".");
        return sb.toString();
    }

    private List<String> buildIncidentsWith(Notes notes) {
        return notes.getNote()
                .stream()
                .filter(note -> Optional.ofNullable(note.getPriority()).isPresent())
                .map(Note::getValue)
                .collect(Collectors.toList());
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
        travelPoint.setStationId(stop.getId());
        travelPoint.setCountry(Locale.GERMANY);
        Optional.ofNullable(stop.getDepDate()).ifPresent(depTime -> travelPoint.setDepartureTime(buildDateFrom(stop.getDepDate().concat("-").concat(stop.getDepTime()))));
        Optional.ofNullable(stop.getArrDate()).ifPresent(arrTime -> travelPoint.setArrivalTime(buildDateFrom(stop.getArrDate().concat("-").concat(stop.getArrTime()))));
        return travelPoint.build();
    }

    private TravelPoint buildTravelPointWith(OriginDestType originDestType) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(originDestType.getLat().doubleValue(), originDestType.getLon().doubleValue()).build());
        travelPoint.setStationName(originDestType.getName());
        travelPoint.setStationId(originDestType.getId());
        Optional.ofNullable(originDestType.getTrack()).ifPresent(travelPoint::setPlatform);
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
