package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.net.URL;

public interface BBCHttpCallBuilderService {

    URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String bbcBuildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    HttpHeaders buildHttpHeadersForBbcWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityForBbc(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
