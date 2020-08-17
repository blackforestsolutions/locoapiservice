package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPointStatus;

public interface OSMApiService {
    CallStatus<TravelPointStatus> getTravelPointFrom(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address);
}
