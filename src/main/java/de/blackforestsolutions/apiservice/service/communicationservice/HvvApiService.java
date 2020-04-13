package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HvvApiService {
    Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    List<TravelPoint> getStationListFromHvvApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
