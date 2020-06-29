package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class HvvApiServiceImpl implements HvvApiService {

    private final CallService callService;
    private final HvvHttpCallBuilderService httpCallBuilderService;
    private final HvvMapperService mapper;

    @Autowired
    public HvvApiServiceImpl(CallService callService, HvvHttpCallBuilderService httpCallBuilderService, HvvMapperService mapper) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapper = mapper;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            ResponseEntity<String> result = buildAndExceuteCall(apiTokenAndUrlInformation);
            return new CallStatus<>(mapper.getJourneyMapFrom(result.getBody()), Status.SUCCESS, null);
        } catch (Exception ex) {
            log.error("Error during calling hvv api: ", ex);
            return new CallStatus<>(null, Status.FAILED, ex);
        }
    }

    @Override
    public CallStatus<List<TravelPoint>> getStationListFromHvvApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String url = getHvvRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildStationListPathWith(apiTokenAndUrlInformation));
            ResponseEntity<String> result = callService.post(url, httpCallBuilderService.buildStationListHttpEntityForHvv(apiTokenAndUrlInformation));
            return new CallStatus<>(mapper.getStationListFrom(result.getBody()), Status.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error while processing json", e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    private ResponseEntity<String> buildAndExceuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws JsonProcessingException {
        String travelPointUrl = getHvvRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildTravelPointPathWith(apiTokenAndUrlInformation));
        String journeyUrl = getHvvRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildJourneyPathWith(apiTokenAndUrlInformation));

        ResponseEntity<String> departureJson = callService.post(travelPointUrl, httpCallBuilderService.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        HvvStation departure = mapper.getHvvStationFrom(departureJson.getBody());

        ResponseEntity<String> destinationJson = callService.post(travelPointUrl, httpCallBuilderService.buildTravelPointHttpEntityForHvv(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        HvvStation destination = mapper.getHvvStationFrom(destinationJson.getBody());

        return callService.post(journeyUrl, httpCallBuilderService.buildJourneyHttpEntityForHvv(apiTokenAndUrlInformation, departure, destination));

    }

    private String getHvvRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String path) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(path);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}
