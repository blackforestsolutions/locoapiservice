package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.AirportsFinderApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashSet;

@RestController
@RequestMapping("nearest-airports")
public class NearestStationFinderController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final AirportsFinderApiService airportsFinderApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "airportsFinderApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation;

    @Autowired
    public NearestStationFinderController(AirportsFinderApiService airportsFinderApiService, ExceptionHandlerService exceptionHandlerService) {
        this.airportsFinderApiService = airportsFinderApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("/get")
    public LinkedHashSet<TravelPoint> retrieveNearestAirportTravelPoints(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptionsTravelPoints(airportsFinderApiService.getAirportsWith(getAirportsFinderApiTokenAndUrlInformation(requestInformation)));
    }

    private ApiTokenAndUrlInformation getAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, airportsFinderApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation) {
        this.airportsFinderApiTokenAndUrlInformation = airportsFinderApiTokenAndUrlInformation;
    }
}
