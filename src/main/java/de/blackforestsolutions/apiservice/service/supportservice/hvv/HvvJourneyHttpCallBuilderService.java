package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.springframework.http.HttpEntity;

public interface HvvJourneyHttpCallBuilderService {
    @SuppressWarnings("rawtypes")
    HttpEntity buildJourneyHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination);

    String buildJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
