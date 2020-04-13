package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;
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
public class LocatorController {

    private final SearchChApiService searchChApiService;

    @Resource(name = "searchApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation searchApiTokenAndUrlInformation;

    @Autowired
    public LocatorController(SearchChApiService searchChApiService) {
        this.searchChApiService = searchChApiService;
    }

    @GetMapping("/locate")
    public Map<UUID, JourneyStatus> retrieveLocatorJourneys(ApiTokenAndUrlInformation request) {
        return new HashMap<>(searchChApiService.getJourneysForRouteWith(getSearchApiTokenAndUrlInformation(request)));
    }

    private ApiTokenAndUrlInformation getSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, searchApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation searchApiTokenAndUrlInformation) {
        this.searchApiTokenAndUrlInformation = searchApiTokenAndUrlInformation;
    }
}
