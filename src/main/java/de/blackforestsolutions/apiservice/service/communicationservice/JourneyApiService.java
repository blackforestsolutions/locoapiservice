package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import org.springframework.data.util.Pair;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

public interface JourneyApiService {
    Flux<String> retrieveJourneysFromApiServices(String userRequest, List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> apiServices);
}
