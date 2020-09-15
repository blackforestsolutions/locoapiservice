package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BlaBlaCarMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BlaBlaCarHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.Optional;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.convertJsonToPojo;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class BlaBlaCarApiServiceImpl implements BlaBlaCarApiService {

    private final CallService callService;
    private final BlaBlaCarHttpCallBuilderService blaBlaCarHttpCallBuilderService;
    private final BlaBlaCarMapperService blaBlaCarMapperService;

    @Autowired
    public BlaBlaCarApiServiceImpl(CallService callService, BlaBlaCarHttpCallBuilderService blaBlaCarHttpCallBuilderService, BlaBlaCarMapperService blaBlaCarMapperService) {
        this.callService = callService;
        this.blaBlaCarHttpCallBuilderService = blaBlaCarHttpCallBuilderService;
        this.blaBlaCarMapperService = blaBlaCarMapperService;
    }

    @Override
    public Flux<CallStatus<Journey>> getJourneysForRouteByCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return Optional.ofNullable(apiTokenAndUrlInformation.getDepartureCoordinates())
                .flatMap(departureCoordinates -> Optional.ofNullable(apiTokenAndUrlInformation.getArrivalCoordinates()))
                .map(arrivalCoordinates -> getJourneysForRouteWith(apiTokenAndUrlInformation))
                .orElse(Flux.empty());
    }

    private Flux<CallStatus<Journey>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return Mono.just(apiTokenAndUrlInformation)
                .map(blaBlaCarHttpCallBuilderService::buildJourneyCoordinatesPathWith)
                .map(path -> getBlaBlaCarRequestString(apiTokenAndUrlInformation, path))
                .flatMap(url -> callService.get(url, HttpEntity.EMPTY))
                .flatMap(response -> convertJsonToPojo(response.getBody(), Rides.class))
                .flatMapMany(blaBlaCarMapperService::buildJourneysWith)
                .onErrorResume(e -> Flux.just(new CallStatus<>(null, Status.FAILED, e)));
    }

    private String getBlaBlaCarRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String path) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(path);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}
