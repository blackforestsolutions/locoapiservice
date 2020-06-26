package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.springframework.http.HttpEntity;

public interface HvvHttpCallBuilderService {
    HttpEntity<String> buildStationListHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildStationListPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity<String> buildJourneyHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination);

    String buildJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity<String> buildTravelPointHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    String buildTravelPointPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
