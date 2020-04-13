package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.DepartureBoard;

import java.util.Map;

public interface BahnDepartureBoardService {

    Map<String, DepartureBoard> getDepartureBoardForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception;
}
