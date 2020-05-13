package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

public interface SearchChHttpCallBuilderService {

    String buildSearchChLocationPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildSearchChRoutePath(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
