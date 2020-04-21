package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

import java.util.Set;

public interface AirportsFinderApiService {
    Set<CallStatus> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
