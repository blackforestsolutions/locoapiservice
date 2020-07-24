package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.HvvApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.NahShApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.RMVApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.VBBApiService;
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
@RequestMapping("regional-train")
public class RegionalTrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final HvvApiService hvvApiService;
    private final RMVApiService rmvApiService;
    private final VBBApiService vbbApiService;
    private final NahShApiService nahShApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "hvvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation;
    @Resource(name = "rmvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation rmvApiTokenAndUrlInformation;
    @Resource(name = "vbbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation;
    @Resource(name = "nahShApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation nahShApiTokenAndUrlInformation;

    @Autowired
    public RegionalTrainRidesController(HvvApiService hvvApiService, RMVApiService rmvApiService, VBBApiService vbbApiService, NahShApiService nahShApiService, ExceptionHandlerService exceptionHandlerService) {
        this.hvvApiService = hvvApiService;
        this.rmvApiService = rmvApiService;
        this.vbbApiService = vbbApiService;
        this.nahShApiService = nahShApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("/get")
    public Map<UUID, Journey> retrieveTrainJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Arrays.asList(
                hvvApiService.getJourneysForRouteWith(getHvvApiTokenAndUrlInformation(requestInformation)),
                rmvApiService.getJourneysForRouteByCoordinatesWith(getRMVApiTokenAndUrlInformation(requestInformation)),
                rmvApiService.getJourneysForRouteBySearchStringWith(getRMVApiTokenAndUrlInformation(requestInformation)),
                vbbApiService.getJourneysForRouteWith(getVbbApiTokenAndUrlInformation(requestInformation)),
                nahShApiService.getJourneysForRouteWith(getNahShApiTokenAndUrlInformation(requestInformation)))
        );
    }

    private ApiTokenAndUrlInformation getHvvApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, hvvApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getRMVApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, rmvApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getVbbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, vbbApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getNahShApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, nahShApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setHvvApiTokenAndUrlInformation(ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation) {
        this.hvvApiTokenAndUrlInformation = hvvApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setRMVApiTokenAndUrlInformation(ApiTokenAndUrlInformation rMVApiTokenAndUrlInformation) {
        this.rmvApiTokenAndUrlInformation = rMVApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setVbbApiTokenAndUrlInformation(ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation) {
        this.vbbApiTokenAndUrlInformation = vbbApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setNahShApiTokenAndUrlInformation(ApiTokenAndUrlInformation nahShApiTokenAndUrlInformation) {
        this.nahShApiTokenAndUrlInformation = nahShApiTokenAndUrlInformation;
    }

}
