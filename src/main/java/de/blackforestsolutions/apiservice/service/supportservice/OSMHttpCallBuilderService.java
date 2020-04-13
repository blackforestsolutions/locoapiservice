package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

import java.net.URL;

public interface OSMHttpCallBuilderService {
    URL buildOSMUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildOSMPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address);
}
