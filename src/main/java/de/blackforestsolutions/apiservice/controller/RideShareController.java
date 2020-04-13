package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BBCApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.controller.RequestTokenHandler.getRequestApiTokenWith;

@RestController
public class RideShareController {

    private final BBCApiService bbcApiService;

    @Resource(name = "bbcApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation;

    @Autowired
    public RideShareController(BBCApiService bbcApiService) {
        this.bbcApiService = bbcApiService;
    }

    @GetMapping("ride-shares")
    public Map<UUID, JourneyStatus> retrieveRideSharingJourneys(ApiTokenAndUrlInformation request) {
        return new HashMap<>(bbcApiService.getJourneysForRouteWith(getBbcApiTokenAndUrlInformation(request)));
    }

    private ApiTokenAndUrlInformation getBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return getRequestApiTokenWith(request, bbcApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation) {
        this.bbcApiTokenAndUrlInformation = bbcApiTokenAndUrlInformation;
    }
}
