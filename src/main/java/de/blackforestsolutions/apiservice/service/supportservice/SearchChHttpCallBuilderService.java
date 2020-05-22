package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

public interface SearchChHttpCallBuilderService {

    String buildSearchChLocationPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location);

    String buildSearchChRoutePath(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
