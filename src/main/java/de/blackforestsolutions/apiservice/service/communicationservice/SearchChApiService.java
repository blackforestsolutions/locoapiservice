package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface SearchChApiService {
    Map<String, TravelPoint> getTravelPointForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws IOException;

    Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
