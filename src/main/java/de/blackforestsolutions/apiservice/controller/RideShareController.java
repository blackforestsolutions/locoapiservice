package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BBCApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.controller.RequestTokenHandler.getRequestApiTokenWith;

@RestController
@RequestMapping("ride-shares")
public class RideShareController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BBCApiService bbcApiService;

    @Resource(name = "bbcApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation;

    @Autowired
    public RideShareController(BBCApiService bbcApiService) {
        this.bbcApiService = bbcApiService;
    }

    @RequestMapping("get")
    public List<CallStatus<Map<UUID, JourneyStatus>>> retrieveRideSharingJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);

       // Map<UUID, JourneyStatus> resultMap = bbcApiService.getJourneysForRouteWith(getBbcApiTokenAndUrlInformation(requestInformation));
       // resultMap.putAll(bbcApiService.getJourneysForRouteByCoordinates(getBbcApiTokenAndUrlInformation(requestInformation)));
        return Arrays.asList(
                bbcApiService.getJourneysForRouteByCoordinates(getBbcApiTokenAndUrlInformation(requestInformation)),
                bbcApiService.getJourneysForRouteWith(getBbcApiTokenAndUrlInformation(requestInformation))
        );
    }

    private ApiTokenAndUrlInformation getBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return getRequestApiTokenWith(request, bbcApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation) {
        this.bbcApiTokenAndUrlInformation = bbcApiTokenAndUrlInformation;
    }
}
