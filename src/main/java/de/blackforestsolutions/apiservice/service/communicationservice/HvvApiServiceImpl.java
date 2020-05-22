package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvJourneyHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvStationListHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvTravelPointHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class HvvApiServiceImpl implements HvvApiService {

    private final CallService callService;
    private final HvvStationListHttpCallBuilderService stationListCallBuilder;
    private final HvvTravelPointHttpCallBuilderService travelPointCallBuilder;
    private final HvvJourneyHttpCallBuilderService journeyCallBuilder;
    private final HvvMapperService mapper;

    @Autowired
    public HvvApiServiceImpl(CallService callService, HvvStationListHttpCallBuilderService stationListCallBuilder, HvvTravelPointHttpCallBuilderService travelPointCallBuilder, HvvJourneyHttpCallBuilderService journeyCallBuilder, HvvMapperService mapper) {
        this.callService = callService;
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
    public CallStatus getStationListFromHvvApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getHvvStationListRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result;
        try {
            result = callService.post(url, stationListCallBuilder.buildStationListHttpEntityForHvv(apiTokenAndUrlInformation));
            return new CallStatus(mapper.getStationListFrom(result.getBody()), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error while processing json", e);
            return new CallStatus(null, Status.FAILED, e);
        }
    }

    private ResponseEntity<String> buildAndExceuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String travelPointUrl = getHvvTravelPointRequestString(apiTokenAndUrlInformation);
        String journeyUrl = getHvvJourneyRequestString(apiTokenAndUrlInformation);

        ResponseEntity<String> departureJson = callService.post(travelPointUrl, travelPointCallBuilder.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        HvvStation departure = mapper.getHvvStationFrom(departureJson.getBody());

        ResponseEntity<String> destinationJson = callService.post(travelPointUrl, travelPointCallBuilder.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        HvvStation destination = mapper.getHvvStationFrom(destinationJson.getBody());

        return callService.post(journeyUrl, journeyCallBuilder.buildJourneyHttpEntityForHvv(apiTokenAndUrlInformation, departure, destination));

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
