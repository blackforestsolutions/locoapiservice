package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

public interface BritishAirwaysApiService {
    CallStatus getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
