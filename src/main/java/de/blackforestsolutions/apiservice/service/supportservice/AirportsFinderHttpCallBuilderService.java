package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.net.URL;

public interface AirportsFinderHttpCallBuilderService {
    URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpHeaders buildHttpHeaderForAirportsFinderWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityAirportsFinder(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
