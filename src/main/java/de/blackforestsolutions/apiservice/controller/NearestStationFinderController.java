package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.AirportsFinderApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPointStatus;
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

    @Resource(name = "airportsFinderApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation;

    @Autowired
    public NearestStationFinderController(AirportsFinderApiService airportsFinderApiService) {
        this.airportsFinderApiService = airportsFinderApiService;
    }

    @RequestMapping("/get")
    public CallStatus<LinkedHashSet<TravelPointStatus>> retrieveAirportsFinderTravelPoints(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);

        return airportsFinderApiService.getAirportsWith(getAirportsFinderApiTokenAndUrlInformation(requestInformation));
        // new LinkedHashSet<>(airportsFinderApiService.getAirportsWith(getAirportsFinderApiTokenAndUrlInformation(requestInformation)));
    }

    private ApiTokenAndUrlInformation getAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, airportsFinderApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation) {
        this.airportsFinderApiTokenAndUrlInformation = airportsFinderApiTokenAndUrlInformation;
    }
}
