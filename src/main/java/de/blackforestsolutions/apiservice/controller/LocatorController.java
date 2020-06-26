package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.OSMApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("locate")
public class LocatorController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final OSMApiService osmApiService;

    @Resource(name = "osmApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation osmApiTokenAndUrlInformation;

    @Autowired
    public LocatorController(OSMApiService osmApiService) {
        this.osmApiService = osmApiService;
    }

    @RequestMapping("/get")
    public CallStatus<Coordinates> retrieveLocatorJourneys(@RequestBody String request) throws IOException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return osmApiService.getCoordinatesFromTravelPointWith(getOsmApiTokenAndUrlInformation(requestInformation), request);
    }

    private ApiTokenAndUrlInformation getOsmApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, osmApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setOsmApiTokenAndUrlInformation(ApiTokenAndUrlInformation osmApiTokenAndUrlInformation) {
        this.osmApiTokenAndUrlInformation = osmApiTokenAndUrlInformation;
    }
}
