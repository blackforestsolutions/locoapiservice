package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class TrainRidesController {

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

    @GetMapping("/rides")
    public Map<UUID, JourneyStatus> retrieveTrainJourneys(ApiTokenAndUrlInformation request) {
        Map<UUID, JourneyStatus> journeys = new HashMap<>();
        journeys.putAll(bahnJourneyDetailsService.getJourneysForRouteWith(getBahnApiTokenAndUrlInformation(request)));
        journeys.putAll(dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(request)));
        return journeys;
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
}
