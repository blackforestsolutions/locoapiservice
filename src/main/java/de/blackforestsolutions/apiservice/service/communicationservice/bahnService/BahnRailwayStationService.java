package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.Map;

public interface BahnRailwayStationService {
    Map<String, TravelPoint> getTravelPointsForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception;
}
