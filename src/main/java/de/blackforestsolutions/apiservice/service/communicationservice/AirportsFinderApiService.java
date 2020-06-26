package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

public interface AirportsFinderApiService {
    CallStatus getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
