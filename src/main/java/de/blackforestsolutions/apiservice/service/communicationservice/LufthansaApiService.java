package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface LufthansaApiService {

    Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    CallStatus getLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
