package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;

public interface OSMApiService {
    CallStatus<Coordinates> getCoordinatesFromTravelPointWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address);
}
