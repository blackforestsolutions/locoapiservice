package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.http.HttpEntity;

public interface RMVHttpCallBuilderService {

    HttpEntity<String> buildHttpEntityForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildLocationStringPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location);

    String buildLocationCoordinatesPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, Coordinates coordinates);

    String buildTripPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
