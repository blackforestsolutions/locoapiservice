package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import reactor.core.publisher.Flux;

public interface BlaBlaCarApiService {

    Flux<CallStatus<Journey>> getJourneysForRouteByCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
