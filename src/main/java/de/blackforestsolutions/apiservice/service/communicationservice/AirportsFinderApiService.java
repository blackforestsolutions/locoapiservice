package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.LinkedHashSet;

public interface AirportsFinderApiService {
    CallStatus<LinkedHashSet<TravelPointStatus>> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
