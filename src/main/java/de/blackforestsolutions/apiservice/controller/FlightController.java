package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BritishAirwaysApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("flights")
public class FlightController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BritishAirwaysApiService britishAirwaysApiService;
    private final LufthansaApiService lufthansaApiService;
    private final ExceptionHandlerService exceptionHandlerService;
    @Resource(name = "britishAirwaysApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation;
    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    public FlightController(BritishAirwaysApiService britishAirwaysApiService, LufthansaApiService lufthansaApiService, ExceptionHandlerService exceptionHandlerService) {
        this.britishAirwaysApiService = britishAirwaysApiService;
        this.lufthansaApiService = lufthansaApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }


    @RequestMapping("/get")
    public Map<UUID, Journey> retrieveFlightJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Arrays.asList(
                britishAirwaysApiService.getJourneysForRouteWith(getBritishAirwaysApiTokenAndUrlInformation(requestInformation)),
                lufthansaApiService.getJourneysForRouteWith(getLufthansaApiTokenAndUrlInformation(requestInformation)))
        );
    }

    private ApiTokenAndUrlInformation getBritishAirwaysApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
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

