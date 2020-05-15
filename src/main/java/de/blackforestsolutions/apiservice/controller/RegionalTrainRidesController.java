package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.HvvApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.NahShApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.RMVApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.VBBApiService;
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
public class RegionalTrainRidesController {

    private final HvvApiService hvvApiService;
    private final RMVApiService rmvApiService;
    private final VBBApiService vbbApiService;
    private final NahShApiService nahShApiService;

    @Resource(name = "hvvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation;
    @Resource(name = "rmvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation rmvApiTokenAndUrlInformation;
    @Resource(name = "vbbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation;
    @Resource(name = "nahShApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation nahShApiTokenAndUrlInformation;

    @Autowired
    public RegionalTrainRidesController(HvvApiService hvvApiService, RMVApiService rmvApiService, VBBApiService vbbApiService, NahShApiService nahShApiService) {
        this.hvvApiService = hvvApiService;
        this.rmvApiService = rmvApiService;
        this.vbbApiService = vbbApiService;
        this.nahShApiService = nahShApiService;
    }

    @GetMapping("regional-train")
    public Map<UUID, JourneyStatus> retrieveTrainJourneys(ApiTokenAndUrlInformation request) {
        Map<UUID, JourneyStatus> journeys = new HashMap<>();
        journeys.putAll(hvvApiService.getJourneysForRouteWith(getHvvApiTokenAndUrlInformation(request)));
        journeys.putAll(rmvApiService.getJourneysForRouteWith(getRMVApiTokenAndUrlInformation(request)));
        journeys.putAll(vbbApiService.getJourneysForRouteWith(getVbbApiTokenAndUrlInformation(request)));
        journeys.putAll(nahShApiService.getJourneysForRouteWith(getNahShApiTokenAndUrlInformation(request)));
        return journeys;
    }

    private ApiTokenAndUrlInformation getHvvApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, hvvApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getRMVApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, rmvApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getVbbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, vbbApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getNahShApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, nahShApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setHvvApiTokenAndUrlInformation(ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation) {
        this.hvvApiTokenAndUrlInformation = hvvApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setRMVApiTokenAndUrlInformation(ApiTokenAndUrlInformation rMVApiTokenAndUrlInformation) {
        this.rmvApiTokenAndUrlInformation = rMVApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setVbbApiTokenAndUrlInformation(ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation) {
        this.vbbApiTokenAndUrlInformation = vbbApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setNahShApiTokenAndUrlInformation(ApiTokenAndUrlInformation nahShApiTokenAndUrlInformation) {
        this.nahShApiTokenAndUrlInformation = nahShApiTokenAndUrlInformation;
    }
}
