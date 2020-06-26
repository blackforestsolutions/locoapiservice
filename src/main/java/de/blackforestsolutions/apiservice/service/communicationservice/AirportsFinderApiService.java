package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.LinkedHashSet;

public interface AirportsFinderApiService {
    LinkedHashSet<CallStatus<TravelPoint>> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
