package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

public interface HvvStationListHttpCallBuilderService {
    @SuppressWarnings("rawtypes")
    HttpEntity buildStationListHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildStationListPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
