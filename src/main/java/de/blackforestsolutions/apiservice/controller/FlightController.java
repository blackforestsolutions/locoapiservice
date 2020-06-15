package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.BritishAirwaysApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
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
@RequestMapping("flights")
public class FlightController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BritishAirwaysApiService britishAirwaysApiService;
    private final LufthansaApiService lufthansaApiService;
    @Resource(name = "britishAirwaysApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation;
    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    public FlightController(BritishAirwaysApiService britishAirwaysApiService, LufthansaApiService lufthansaApiService) {
        this.britishAirwaysApiService = britishAirwaysApiService;
        this.lufthansaApiService = lufthansaApiService;
    }


    @RequestMapping("/get")
    public Map<UUID, JourneyStatus> flights(@RequestBody String request) throws JsonProcessingException {
        final Map<UUID, JourneyStatus> resultMap = new HashMap<>();
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        // todo when executing FlightControllerTest I can't get into getJourneyFor... despite setting breakpoints
        CallStatus britishAirwaysCallStatus = this.britishAirwaysApiService.getJourneysForRouteWith(getBritishAirwaysApiTokenAndUrlInformation(requestInformation));

        if (britishAirwaysCallStatus != null) {
            if (britishAirwaysCallStatus.getCalledObject() != null) {
                Optional.ofNullable(britishAirwaysCallStatus).ifPresent(britishAirwaysFlights -> resultMap.putAll((Map<UUID, JourneyStatus>) britishAirwaysCallStatus.getCalledObject()));
            }
        }
        // Optional.ofNullable(britishAirwaysCallStatus).ifPresent(britishAirwaysFlights -> resultMap.putAll((Map<UUID, JourneyStatus>) britishAirwaysCallStatus.getCalledObject()));

        CallStatus lufthansaCallStatus = this.lufthansaApiService.getJourneysForRouteWith(getLufthansaApiTokenAndUrlInformation(requestInformation));
        if (lufthansaCallStatus != null) {
            if (lufthansaCallStatus.getCalledObject() != null) {
                Optional.ofNullable(lufthansaCallStatus.getCalledObject()).ifPresent(lufthansaFlights -> resultMap.putAll((Map<UUID, JourneyStatus>) lufthansaCallStatus.getCalledObject()));
            }
        }
        // Optional.ofNullable(lufthansaCallStatus.getCalledObject()).ifPresent(lufthansaFlights -> resultMap.putAll((Map<UUID, JourneyStatus>) lufthansaCallStatus.getCalledObject()));

        return resultMap;
    }

    private ApiTokenAndUrlInformation getBritishAirwaysApiTokenAndUrlInformation(
            ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, britishAirwaysApiTokenAndUrlInformation);
    }

    private ApiTokenAndUrlInformation getLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, lufthansaApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setBritishAirwaysApiTokenAndUrlInformation(ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation) {
        this.britishAirwaysApiTokenAndUrlInformation = britishAirwaysApiTokenAndUrlInformation;
    }

    @VisibleForTesting
    void setLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation) {
        this.lufthansaApiTokenAndUrlInformation = lufthansaApiTokenAndUrlInformation;
    }
}

