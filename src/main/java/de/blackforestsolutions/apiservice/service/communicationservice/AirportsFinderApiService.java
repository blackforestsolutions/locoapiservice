package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.Set;

public interface AirportsFinderApiService {
    Set<TravelPoint> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
