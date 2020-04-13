package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;

public interface OSMApiService {
    CallStatus getCoordinatesFromTravelPointWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address);
}
