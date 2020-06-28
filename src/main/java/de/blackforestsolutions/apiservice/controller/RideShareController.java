package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BBCApiService;
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

import static de.blackforestsolutions.apiservice.controller.RequestTokenHandler.getRequestApiTokenWith;

@RestController
@RequestMapping("ride-shares")
public class RideShareController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BBCApiService bbcApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "bbcApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation;

    @Autowired
    public RideShareController(BBCApiService bbcApiService, ExceptionHandlerService exceptionHandlerService) {
        this.bbcApiService = bbcApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("get")
    public Map<UUID, Journey> retrieveRideSharingJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Arrays.asList(
                bbcApiService.getJourneysForRouteByCoordinates(getBbcApiTokenAndUrlInformation(requestInformation)),
                bbcApiService.getJourneysForRouteWith(getBbcApiTokenAndUrlInformation(requestInformation)))
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
