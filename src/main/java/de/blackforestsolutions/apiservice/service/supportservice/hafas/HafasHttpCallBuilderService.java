package de.blackforestsolutions.apiservice.service.supportservice.hafas;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface HafasHttpCallBuilderService {

    HttpEntity<String> buildHttpEntityStationForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    HttpEntity<String> buildHttpEntityJourneyForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
