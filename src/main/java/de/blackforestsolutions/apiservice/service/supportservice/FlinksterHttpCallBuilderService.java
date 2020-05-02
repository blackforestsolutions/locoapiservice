package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

import java.net.URL;

public interface FlinksterHttpCallBuilderService {
    URL buildFlinksterUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String buildFlinksterPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpEntity buildHttpEntityForFlinkster(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
