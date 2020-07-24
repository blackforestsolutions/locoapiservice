package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("train-rides")
public class NationalTrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BahnJourneyDetailsService bahnJourneyDetailsService;
    private final SearchChApiService searchChApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "bahnApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation;
    @Resource(name = "searchApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation searchApiTokenAndUrlInformation;

    @Autowired
    public NationalTrainRidesController(BahnJourneyDetailsService bahnJourneyDetailsService, SearchChApiService searchChApiService, ExceptionHandlerService exceptionHandlerService) {
        this.bahnJourneyDetailsService = bahnJourneyDetailsService;
        this.searchChApiService = searchChApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("/get")
    public Map<UUID, Journey> retrieveTrainJourneys(@RequestBody String request) throws IOException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Arrays.asList(
                bahnJourneyDetailsService.getJourneysForRouteWith(getBahnApiTokenAndUrlInformation(requestInformation)),
                searchChApiService.getJourneysForRouteWith(getSearchApiTokenAndUrlInformation(requestInformation)))
        );
    }

    private ApiTokenAndUrlInformation getBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, bahnApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, searchApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation) {
        this.bahnApiTokenAndUrlInformation = bahnApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformation searchApiTokenAndUrlInformation) {
        this.searchApiTokenAndUrlInformation = searchApiTokenAndUrlInformation;
    }

}
