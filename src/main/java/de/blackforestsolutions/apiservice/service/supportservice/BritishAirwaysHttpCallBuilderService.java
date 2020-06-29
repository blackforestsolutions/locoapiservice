package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public interface BritishAirwaysHttpCallBuilderService {

    String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpHeaders buildHttpHeadersForBritishAirwaysWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity<String> buildHttpEntityBritishAirways(ApiTokenAndUrlInformation apiTokenAndUrlInformation);


}
