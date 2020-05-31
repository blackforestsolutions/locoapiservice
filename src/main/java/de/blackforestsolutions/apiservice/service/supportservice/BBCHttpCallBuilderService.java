package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

public interface BBCHttpCallBuilderService {
    String bbcBuildJourneyStringPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String bbcBuildJourneyCoordinatesPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
