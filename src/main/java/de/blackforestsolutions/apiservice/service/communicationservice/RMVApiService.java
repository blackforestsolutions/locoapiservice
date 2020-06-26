package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface RMVApiService {
    CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteBySearchStringWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteByCoordinatesWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
