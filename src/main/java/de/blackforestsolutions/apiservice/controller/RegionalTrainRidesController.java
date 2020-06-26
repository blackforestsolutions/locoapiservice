package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.HvvApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.NahShApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.RMVApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.VBBApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("regional-train")
public class RegionalTrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final HvvApiService hvvApiService;
    private final RMVApiService rmvApiService;
    private final VBBApiService vbbApiService;
    private final NahShApiService nahShApiService;

    @Resource(name = "hvvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation;
    @Resource(name = "rmvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation rmvApiTokenAndUrlInformation;
    @Resource(name = "vbbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation;
    @Resource(name = "nahShApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation nahShApiTokenAndUrlInformation;

    @Autowired
    public RegionalTrainRidesController(HvvApiService hvvApiService, RMVApiService rmvApiService, VBBApiService vbbApiService, NahShApiService nahShApiService) {
        this.hvvApiService = hvvApiService;
        this.rmvApiService = rmvApiService;
        this.vbbApiService = vbbApiService;
        this.nahShApiService = nahShApiService;
    }

    @RequestMapping("/get")
    public Map<UUID, JourneyStatus> retrieveTrainJourneys(@RequestBody String request) throws JsonProcessingException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return mapJourneyResults(requestInformation);
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

    private Map<UUID, JourneyStatus> mapJourneyResults(ApiTokenAndUrlInformation requestToken) {
        final Map<UUID, JourneyStatus> journeys = new HashMap<>();

        CallStatus<Map<UUID, JourneyStatus>> hvvJourneyStatus = hvvApiService.getJourneysForRouteWith(getHvvApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(hvvJourneyStatus).isPresent() && Optional.ofNullable(hvvJourneyStatus.getCalledObject()).isPresent() && hvvJourneyStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(hvvJourneyStatus.getCalledObject());
        }

        CallStatus<Map<UUID, JourneyStatus>> rmvJourneyByStringStatus = rmvApiService.getJourneysForRouteBySearchStringWith(getRMVApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(rmvJourneyByStringStatus).isPresent() && Optional.ofNullable(rmvJourneyByStringStatus.getCalledObject()).isPresent() && rmvJourneyByStringStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(rmvJourneyByStringStatus.getCalledObject());
        }

        CallStatus<Map<UUID, JourneyStatus>> rmvJourneyStatusByCoordinates = rmvApiService.getJourneysForRouteByCoordinatesWith(getRMVApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(rmvJourneyStatusByCoordinates).isPresent() && Optional.ofNullable(rmvJourneyStatusByCoordinates.getCalledObject()).isPresent() && rmvJourneyStatusByCoordinates.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(rmvJourneyStatusByCoordinates.getCalledObject());
        }

        CallStatus<Map<UUID, JourneyStatus>> vbbJourneyStatus = vbbApiService.getJourneysForRouteWith(getVbbApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(vbbJourneyStatus).isPresent() && Optional.ofNullable(vbbJourneyStatus.getCalledObject()).isPresent() && vbbJourneyStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(vbbJourneyStatus.getCalledObject());
        }

        CallStatus<Map<UUID, JourneyStatus>> nahShJourneyStatus = nahShApiService.getJourneysForRouteWith(getNahShApiTokenAndUrlInformation(requestToken));
        if (Optional.ofNullable(nahShJourneyStatus).isPresent() && Optional.ofNullable(nahShJourneyStatus.getCalledObject()).isPresent() && nahShJourneyStatus.getStatus().equals(Status.SUCCESS)) {
            journeys.putAll(nahShJourneyStatus.getCalledObject());
        }

        return journeys;
    }
}
