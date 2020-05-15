package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface RMVHttpCallBuilderService {

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityStationForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityTripForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildLocationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location);

    String buildTripPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
