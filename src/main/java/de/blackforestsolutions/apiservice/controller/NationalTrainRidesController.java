package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("train-rides")
public class NationalTrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BahnJourneyDetailsService bahnJourneyDetailsService;
    private final DBApiService dbApiService;
    private final SearchChApiService searchChApiService;

    @Resource(name = "bahnApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation;
    @Resource(name = "dbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation dbApiTokenAndUrlInformation;
    @Resource(name = "searchApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation searchApiTokenAndUrlInformation;

    @Autowired
    public NationalTrainRidesController(BahnJourneyDetailsService bahnJourneyDetailsService, DBApiService dbApiService, SearchChApiService searchChApiService) {
        this.bahnJourneyDetailsService = bahnJourneyDetailsService;
        this.dbApiService = dbApiService;
        this.searchChApiService = searchChApiService;
    }

    @RequestMapping("/get")
    public List<CallStatus<Map<UUID, JourneyStatus>>> retrieveTrainJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return Arrays.asList(
                bahnJourneyDetailsService.getJourneysForRouteWith(getBahnApiTokenAndUrlInformation(requestInformation)),
                dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(requestInformation)),
                searchChApiService.getTravelPointForRouteFromApiWith(getSearchApiTokenAndUrlInformation(requestInformation))
        );
    }

    private ApiTokenAndUrlInformation getBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, bahnApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, dbApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, searchApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation) {
        this.bahnApiTokenAndUrlInformation = bahnApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation dbApiTokenAndUrlInformation) {
        this.dbApiTokenAndUrlInformation = dbApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation searchApiTokenAndUrlInformation) {
        this.searchApiTokenAndUrlInformation = searchApiTokenAndUrlInformation;
    }

/*    private Map<UUID, JourneyStatus> mapJourneyResults(ApiTokenAndUrlInformation requestToken) {
        final Map<UUID, JourneyStatus> journeys = new HashMap<>();
        journeys.putAll(bahnJourneyDetailsService.getJourneysForRouteWith(getBahnApiTokenAndUrlInformation(requestToken)));

        CallStatus<Map<UUID, JourneyStatus>> dbJourneyStatus = dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(dbJourneyStatus).isPresent() && Optional.ofNullable(dbJourneyStatus.getCalledObject()).isPresent() && dbJourneyStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(dbJourneyStatus.getCalledObject());
        }

        return journeys;
    }*/
}
