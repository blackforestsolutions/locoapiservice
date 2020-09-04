package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.service.communicationservice.BlaBlaCarApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.JourneyApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("ride-shares")
public class RideShareController {

    private final JourneyApiService journeyApiService;
    private final BlaBlaCarApiService blaBlaCarApiService;
    private final ApiTokenAndUrlInformation blaBlaCarApiTokenAndUrlInformation;

    @Autowired
    public RideShareController(JourneyApiService journeyApiService, BlaBlaCarApiService blaBlaCarApiService, ApiTokenAndUrlInformation blaBlaCarApiTokenAndUrlInformation) {
        this.journeyApiService = journeyApiService;
        this.blaBlaCarApiService = blaBlaCarApiService;
        this.blaBlaCarApiTokenAndUrlInformation = blaBlaCarApiTokenAndUrlInformation;
    }

    @RequestMapping(value = "/get", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> retrieveRideSharingJourneys(@RequestBody String request) {
        return Optional.ofNullable(request)
                .map(r -> journeyApiService.retrieveJourneysFromApiServices(r, getApiTokensWithApiServices()))
                .orElseGet(() -> {
                    log.warn("No provided request body!");
                    return Flux.empty();
                });
    }

    private List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> getApiTokensWithApiServices() {
        return List.of(
                Pair.of(blaBlaCarApiTokenAndUrlInformation, blaBlaCarApiService::getJourneysForRouteByCoordinates)
        );
    }
}
