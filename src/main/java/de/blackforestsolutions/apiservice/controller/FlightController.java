package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BritishAirwaysApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@RestController
public class FlightController {

    private final BritishAirwaysApiService britishAirwaysApiService;
    private final LufthansaApiService lufthansaApiService;
    @Resource(name = "britishAirwaysApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation;
    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    public FlightController(BritishAirwaysApiService britishAirwaysApiService, LufthansaApiService lufthansaApiService) {
        this.britishAirwaysApiService = britishAirwaysApiService;
        this.lufthansaApiService = lufthansaApiService;
    }

    @GetMapping("/flights")
    public Map<UUID, JourneyStatus> flights(@RequestBody ApiTokenAndUrlInformation requestInformation) {
        Map<UUID, JourneyStatus> resultMap = this.britishAirwaysApiService.getJourneysForRouteWith(getBritishAirwaysApiTokenAndUrlInformation(requestInformation));
        resultMap.putAll(this.lufthansaApiService.getJourneysForRouteWith(getLufthansaApiTokenAndUrlInformation(requestInformation)));
        return resultMap;
    }

    private ApiTokenAndUrlInformation getBritishAirwaysApiTokenAndUrlInformation(
            ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, britishAirwaysApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, lufthansaApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBritishAirwaysApiTokenAndUrlInformation(ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation) {
        this.britishAirwaysApiTokenAndUrlInformation = britishAirwaysApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation) {
        this.lufthansaApiTokenAndUrlInformation = lufthansaApiTokenAndUrlInformation;
    }
}

