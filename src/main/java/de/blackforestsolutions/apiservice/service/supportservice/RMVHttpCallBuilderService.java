package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

import java.net.URL;

public interface RMVHttpCallBuilderService {

    URL buildRMVUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityStationForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityTripForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildLocationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location);

    String buildTripPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
