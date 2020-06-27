package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.bahn.JourneyDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static java.util.Collections.EMPTY_LIST;

@Service
@Slf4j
public class BahnJourneyDetailsServiceImpl implements BahnJourneyDetailsService {

    private static final int JOURNEY_DETAIL_COMBINATION_SIZE = 2;
    private static final int START = 0;
    private static final int DESTINATION = 1;

    private final CallService callService;
    private final BahnHttpCallBuilderService bahnHttpCallBuilderService;
    private final UuidService uuidService;

    @Autowired
    public BahnJourneyDetailsServiceImpl(CallService callService, BahnHttpCallBuilderService bahnHttpCallBuilderService, UuidService uuidService) {
        this.bahnHttpCallBuilderService = bahnHttpCallBuilderService;
        this.callService = callService;
        this.uuidService = uuidService;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url;
        try {
            url = getBahnJourneyDetailsRequestString(apiTokenAndUrlInformation);
            ResponseEntity<String> result = callService.get(url, bahnHttpCallBuilderService.buildHttpEntityForBahn(apiTokenAndUrlInformation));
            return new CallStatus<>(map(result.getBody()), Status.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error during calling Deutsche Bahn api", e);
            return new CallStatus<>(Collections.singletonMap(uuidService.createUUID(), JourneyStatusBuilder.createJourneyStatusProblemWith(List.of(e), EMPTY_LIST)), Status.FAILED, e);
        }
    }

    private String getBahnJourneyDetailsRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setApiVersion(apiTokenAndUrlInformation.getApiVersion());
        builder.setGermanRailJourneyDeatilsPath(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath());
        builder.setJourneyDetailsId(apiTokenAndUrlInformation.getJourneyDetailsId());
        builder.setPath(bahnHttpCallBuilderService.buildBahnJourneyDetailsPath(builder.build()));
        return buildUrlWith(builder.build()).toString();
    }

    private Map<UUID, JourneyStatus> map(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<JourneyDetail> journeyDetails = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, JourneyDetail.class));
        return mapJourneyDetailsListToJourneyList(journeyDetails);
    }

    private Map<UUID, JourneyStatus> mapJourneyDetailsListToJourneyList(List<JourneyDetail> journeyDetails) {
        Map<UUID, Journey> journeys = new HashMap<>();
        //noinspection UnstableApiUsage (justification: we will use it and asume it gets into a release)
        Set<Set<JourneyDetail>> combinationSets = Sets.combinations(
                ImmutableSet.copyOf(journeyDetails),
                JOURNEY_DETAIL_COMBINATION_SIZE
        );
        combinationSets
                .forEach(sets -> buildJourneyMapWith(sets, journeys));
        return journeys
                .values()
                .stream()
                .map(JourneyStatusBuilder::createJourneyStatusWith)
                .collect(Collectors.toMap(JourneyStatusBuilder::extractJourneyUuidFrom, journeyStatus -> journeyStatus));
    }

    private void buildJourneyMapWith(Set<JourneyDetail> sets, Map<UUID, Journey> journeys) {
        ArrayList<JourneyDetail> journeyDetailsStop = new ArrayList<>(sets);
        Journey journey = buildJourneyWith(journeyDetailsStop);
        journeys.put(journey.getId(), journey);
    }

    private Journey buildJourneyWith(List<JourneyDetail> journeyDetailsStop) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(uuidService.createUUID());
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        Leg leg = buildLegWith(journeyDetailsStop);
        legs.put(leg.getId(), leg);
        journey.setLegs(legs);
        return journey.build();
    }

    private Leg buildLegWith(List<JourneyDetail> journeyDetailsStop) {
        Leg.LegBuilder journey = new Leg.LegBuilder(uuidService.createUUID());
        journey.setStart(buildTravelPointWith(journeyDetailsStop.get(START)));
        journey.setDestination(buildTravelPointWith(journeyDetailsStop.get(DESTINATION)));
        journey.setTravelProvider(TravelProvider.DB);
        journey.setStartTime(buildTimeFrom(journeyDetailsStop.get(START).getDepTime()));
        journey.setArrivalTime(buildTimeFrom(journeyDetailsStop.get(DESTINATION).getArrTime()));
        journey.setDuration(buildDurationBetween(
                journey.getStartTime(),
                journey.getArrivalTime()
        ));
        journey.setVehicleName(journeyDetailsStop.get(START).getType());
        journey.setVehicleNumber(journeyDetailsStop.get(START).getTrain());

        int startStopIndex = journeyDetailsStop.indexOf(journeyDetailsStop.get(START));
        int endStopIndex = journeyDetailsStop.indexOf(journeyDetailsStop.get(DESTINATION));

        List<JourneyDetail> betweenHolds = journeyDetailsStop.stream()
                .filter(stop -> journeyDetailsStop.indexOf(stop) > startStopIndex && journeyDetailsStop.indexOf(stop) < endStopIndex)
                .collect(Collectors.toList());

        journey.setTravelLine(buildTravelLine(betweenHolds));
        return journey.build();
    }

    private TravelLine buildTravelLine(List<JourneyDetail> betweenHolds) {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setBetweenHolds(convertBetweenHoldsListToMap(betweenHolds));
        return travelLine.build();
    }

    private TravelPoint buildTravelPointWith(JourneyDetail journeyDetail) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId(journeyDetail.getStopId());
        travelPoint.setStationName(journeyDetail.getStopName());
        travelPoint.setGpsCoordinates(
                new Coordinates.CoordinatesBuilder(
                        Double.parseDouble(journeyDetail.getLat()),
                        Double.parseDouble(journeyDetail.getLon())
                ).build()
        );
        return travelPoint.build();
    }

    private HashMap<Integer, TravelPoint> convertBetweenHoldsListToMap(List<JourneyDetail> journeyDetails) {
        HashMap<Integer, TravelPoint> holdsBetween = new HashMap<>();
        int counter = 0;
        for (JourneyDetail journeyDetail : journeyDetails) {
            holdsBetween.put(counter++, buildTravelPointWith(journeyDetail));
        }
        return holdsBetween;
    }


    private Date buildTimeFrom(String time) {
        try {
            return new SimpleDateFormat("HH:mm").parse(time);
        } catch (ParseException e) {
            log.error("could not parse time will use now instead", e);
            return new Date();
        }
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(),
                ZoneId.systemDefault());
    }

    private Duration buildDurationBetween(Date departure, Date arrival) {
        return Duration.between(
                convertToLocalDateTime(departure),
                convertToLocalDateTime(arrival)
        );
    }

}
