package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface AirportsFinderHttpCallBuilderService {

    String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity<String> buildHttpEntityAirportsFinder(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
