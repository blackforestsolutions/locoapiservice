package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface HvvTravelPointHttpCallBuilderService {
    @SuppressWarnings("rawtypes")
    HttpEntity buildTravelPointHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station);

    String buildTravelPointPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
