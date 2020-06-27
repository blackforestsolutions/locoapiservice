package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.LinkedHashSet;

public interface AirportsFinderApiService {
    CallStatus<LinkedHashSet<TravelPointStatus>> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws JsonProcessingException;
}
