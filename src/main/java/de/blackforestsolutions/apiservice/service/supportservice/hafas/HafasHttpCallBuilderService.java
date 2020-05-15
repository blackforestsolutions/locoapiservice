package de.blackforestsolutions.apiservice.service.supportservice.hafas;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface HafasHttpCallBuilderService {

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityStationForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityJourneyForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
