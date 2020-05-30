package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface RMVApiService {
    Map<UUID, JourneyStatus> getJourneysForRouteBySearchStringWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    Map<UUID, JourneyStatus> getJourneysForRouteByCoordinatesWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
