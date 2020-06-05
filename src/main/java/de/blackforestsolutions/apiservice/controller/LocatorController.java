package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("locate")
public class LocatorController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final SearchChApiService searchChApiService;

    @Resource(name = "searchApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation searchApiTokenAndUrlInformation;

    @Autowired
    public LocatorController(SearchChApiService searchChApiService) {
        this.searchChApiService = searchChApiService;
    }

    @RequestMapping("/get")
    public Map<UUID, JourneyStatus> retrieveLocatorJourneys(@RequestBody String request) throws IOException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return new HashMap<>(searchChApiService.getJourneysForRouteWith(getSearchApiTokenAndUrlInformation(requestInformation)));
    }

    private ApiTokenAndUrlInformation getSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, searchApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation searchApiTokenAndUrlInformation) {
        this.searchApiTokenAndUrlInformation = searchApiTokenAndUrlInformation;
    }
}
