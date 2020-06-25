package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

public interface LufthansaApiService {

    CallStatus getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
