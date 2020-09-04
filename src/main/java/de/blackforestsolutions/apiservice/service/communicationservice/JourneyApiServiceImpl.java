package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.controller.RequestTokenHandler;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Function;

import static de.blackforestsolutions.apiservice.controller.RequestTokenHandler.*;


@Slf4j
@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final ExceptionHandlerService exceptionHandlerService;

    @Autowired
    public JourneyApiServiceImpl(ExceptionHandlerService exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @Override
    public Flux<String> retrieveJourneysFromApiServices(String userRequest, List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> apiServices) {
        return Mono.just(userRequest)
                .flatMap(RequestTokenHandler::mapStringToRequestApiToken)
                .flatMapMany(userRequestApiToken -> mergeUserRequestApiTokenWithApiServices(userRequestApiToken, apiServices))
                .flatMap(this::executeApiServicesParallelWith)
                .flatMap(exceptionHandlerService::handleExceptions)
                .distinct(Journey::getId)
                .flatMap(this::mapJourneyToJson)
                .onErrorResume(exceptionHandlerService::handleExceptions);
    }

    private Flux<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> mergeUserRequestApiTokenWithApiServices(ApiTokenAndUrlInformation userRequestApiToken, List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> apiServices) {
        return Flux.fromIterable(apiServices)
                .map(apiServicePair -> Pair.of(getRequestApiTokenWith(userRequestApiToken, apiServicePair.getFirst()), apiServicePair.getSecond()));
    }

    private Flux<CallStatus<Journey>> executeApiServicesParallelWith(Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>> apiService) {
        return Mono.just(apiService)
                .flatMapMany(servicePair -> servicePair.getSecond().apply(servicePair.getFirst()))
                .subscribeOn(Schedulers.parallel());
    }

    private Mono<String> mapJourneyToJson(Journey journey) {
        try {
            return Mono.just(locoJsonMapper.map(journey));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
