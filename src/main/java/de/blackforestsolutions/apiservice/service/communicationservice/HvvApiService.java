package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HvvApiService {
    CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    CallStatus<List<TravelPoint>> getStationListFromHvvApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
