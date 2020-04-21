package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

import java.util.LinkedHashSet;

public interface AirportsFinderApiService {
    LinkedHashSet<CallStatus> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
