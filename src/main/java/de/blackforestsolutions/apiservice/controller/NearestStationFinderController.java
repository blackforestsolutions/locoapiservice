package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.AirportsFinderApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashSet;

@RestController
public class NearestStationFinderController {
    private final AirportsFinderApiService airportsFinderApiService;

    @Resource(name = "airportsFinderApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation;

    @Autowired
    public NearestStationFinderController(AirportsFinderApiService airportsFinderApiService) {
        this.airportsFinderApiService = airportsFinderApiService;
    }

    @GetMapping("/nearestairports")
    public LinkedHashSet<CallStatus> retrieveAirportsFinderTravelPoints(ApiTokenAndUrlInformation request) {
        return new LinkedHashSet<>(airportsFinderApiService.getAirportsWith(getAirportsFinderApiTokenAndUrlInformation(request)));
    }

    private ApiTokenAndUrlInformation getAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, airportsFinderApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation) {
        this.airportsFinderApiTokenAndUrlInformation = airportsFinderApiTokenAndUrlInformation;
    }
}
