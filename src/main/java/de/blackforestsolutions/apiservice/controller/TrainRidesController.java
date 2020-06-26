package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("train-rides")
public class TrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BahnJourneyDetailsService bahnJourneyDetailsService;
    private final DBApiService dbApiService;

    @Resource(name = "bahnApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation;
    @Resource(name = "dbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation dbApiTokenAndUrlInformation;

    @Autowired
    public TrainRidesController(BahnJourneyDetailsService bahnJourneyDetailsService, DBApiService dbApiService) {
        this.bahnJourneyDetailsService = bahnJourneyDetailsService;
        this.dbApiService = dbApiService;
    }

    @RequestMapping("/get")
    public Map<UUID, JourneyStatus> retrieveTrainJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return mapJourneyResults(requestInformation);
    }

    private ApiTokenAndUrlInformation getBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, bahnApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, dbApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation) {
        this.bahnApiTokenAndUrlInformation = bahnApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation dbApiTokenAndUrlInformation) {
        this.dbApiTokenAndUrlInformation = dbApiTokenAndUrlInformation;
    }

    private Map<UUID, JourneyStatus> mapJourneyResults(ApiTokenAndUrlInformation requestToken) {
        final Map<UUID, JourneyStatus> journeys = new HashMap<>();
        journeys.putAll(bahnJourneyDetailsService.getJourneysForRouteWith(getBahnApiTokenAndUrlInformation(requestToken)));

        CallStatus<Map<UUID, JourneyStatus>> dbJourneyStatus = dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(dbJourneyStatus).isPresent() && Optional.ofNullable(dbJourneyStatus.getCalledObject()).isPresent() && dbJourneyStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(dbJourneyStatus.getCalledObject());
        }

        return journeys;
    }
}
