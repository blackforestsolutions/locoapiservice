package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HvvCallService;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvJourneyHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvStationListHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvTravelPointHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class HvvApiServiceImpl implements HvvApiService {

    private final HvvCallService hvvCallService;
    private final HvvStationListHttpCallBuilderService stationListCallBuilder;
    private final HvvTravelPointHttpCallBuilderService travelPointCallBuilder;
    private final HvvJourneyHttpCallBuilderService journeyCallBuilder;
    private final HvvMapperService mapper;

    @Autowired
    public HvvApiServiceImpl(HvvCallService hvvCallService, HvvStationListHttpCallBuilderService stationListCallBuilder, HvvTravelPointHttpCallBuilderService travelPointCallBuilder, HvvJourneyHttpCallBuilderService journeyCallBuilder, HvvMapperService mapper) {
        this.hvvCallService = hvvCallService;
        this.stationListCallBuilder = stationListCallBuilder;
        this.travelPointCallBuilder = travelPointCallBuilder;
        this.journeyCallBuilder = journeyCallBuilder;
        this.mapper = mapper;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ResponseEntity<String> result = buildAndExceuteCall(apiTokenAndUrlInformation);
        return mapper.getJourneyMapFrom(result.getBody());
    }

    @Override
    public List<TravelPoint> getStationListFromHvvApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getHvvStationListRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result;
        List<TravelPoint> emptyErrorList = new ArrayList<>();
        try {
            result = hvvCallService.postStationList(url, stationListCallBuilder.buildStationListHttpEntityForHvv(apiTokenAndUrlInformation));
            return mapper.getStationListFrom(result.getBody());
        } catch (JsonProcessingException e) {
            log.error("Error while processing json", e);
            TravelPoint.TravelPointBuilder errorPoint = new TravelPoint.TravelPointBuilder();
            errorPoint.setAirportName("Error" + e.getMessage());
            emptyErrorList.add(errorPoint.build());
            return emptyErrorList;
        }
    }

    private ResponseEntity<String> buildAndExceuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String travelPointUrl = getHvvTravelPointRequestString(apiTokenAndUrlInformation);
        String journeyUrl = getHvvJourneyRequestString(apiTokenAndUrlInformation);

        ResponseEntity<String> departureJson = hvvCallService.postTravelPoint(travelPointUrl, travelPointCallBuilder.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        HvvStation departure = mapper.getHvvStationFrom(departureJson.getBody());

        ResponseEntity<String> destinationJson = hvvCallService.postTravelPoint(travelPointUrl, travelPointCallBuilder.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        HvvStation destination = mapper.getHvvStationFrom(destinationJson.getBody());

        return hvvCallService.postJourney(journeyUrl, journeyCallBuilder.buildJourneyHttpEntityForHvv(apiTokenAndUrlInformation, departure, destination));

    }


    private String getHvvTravelPointRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = buildFrom(apiTokenAndUrlInformation);
        builder.setPath(travelPointCallBuilder.buildTravelPointPathWith(builder.build()));

        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getHvvJourneyRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = buildFrom(apiTokenAndUrlInformation);
        builder.setPath(journeyCallBuilder.buildJourneyPathWith(builder.build()));

        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getHvvStationListRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = buildFrom(apiTokenAndUrlInformation);
        builder.setPath(stationListCallBuilder.buildStationListPathWith(builder.build()));

        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder buildFrom(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);

        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setJourneyPathVariable(apiTokenAndUrlInformation.getJourneyPathVariable());
        builder.setStationListPathVariable(apiTokenAndUrlInformation.getStationListPathVariable());
        builder.setTravelPointPathVariable(apiTokenAndUrlInformation.getTravelPointPathVariable());

        return builder;
    }


}
