package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public interface BahnHttpCallBuilderService {

    String buildBahnArrivalBoardPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildBahnRailwayStationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildBahnDepartureBoardPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildBahnJourneyDetailsPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpHeaders buildHttpHeadersForBahnWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityForBahn(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
