package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public interface LuftHansaHttpCallBuilderService {

    /**
     * This method is to build the Lufthansa journey path string basically without arrival date.
     *
     * @param apiTokenAndUrlInformation contains all relevant information
     * @return path
     */
    String buildLufthansaJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    /**
     * This method is to build the Lufthansa authorization path string.
     *
     * @param apiTokenAndUrlInformation contains all relevant information
     * @return path
     */
    String buildLufthansaAuthorizationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity<String> buildHttpEntityForLufthansaJourney(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity<MultiValueMap<String, String>> buildHttpEntityForLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

}
