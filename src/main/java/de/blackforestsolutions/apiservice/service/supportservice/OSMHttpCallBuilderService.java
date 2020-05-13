package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

public interface OSMHttpCallBuilderService {

    String buildOSMPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address);
}
