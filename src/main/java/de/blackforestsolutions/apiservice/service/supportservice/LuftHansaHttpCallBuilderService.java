package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;

import java.net.URL;

public interface LuftHansaHttpCallBuilderService {

    /**
     * This method is to build the Lufthansa URL.
     *
     * @param apiTokenAndUrlInformation incoming information from frontend
     * @return lufthansaUrl
     */
    URL buildLuftHansaUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    /**
     * This method is to build the Lufthansa path string basically without arrival date.
     *
     * @param apiTokenAndUrlInformation contains all relevant information
     * @return path
     */
    String buildLuftHansaPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    @SuppressWarnings("rawtypes")
    HttpEntity buildHttpEntityForLuftHansa(ApiTokenAndUrlInformation apiTokenAndUrlInformation);

}
