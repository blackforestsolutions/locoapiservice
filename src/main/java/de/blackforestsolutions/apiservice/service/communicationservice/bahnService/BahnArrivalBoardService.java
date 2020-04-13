package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.ArrivalBoard;

import java.util.Map;

public interface BahnArrivalBoardService {
    Map<String, ArrivalBoard> getArrivalBoardForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception;
}
