package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class BBCApiServiceImpl implements BBCApiService {

    private final CallService callService;
    private final BBCHttpCallBuilderService bbcHttpCallBuilderService;
    private final BBCMapperService bbcMapperService;

    @Autowired
    public BBCApiServiceImpl(CallService callService, BBCHttpCallBuilderService bbcHttpCallBuilderService, BBCMapperService bbcMapperService) {
        this.callService = callService;
        this.bbcHttpCallBuilderService = bbcHttpCallBuilderService;
        this.bbcMapperService = bbcMapperService;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getBbcRequestString(apiTokenAndUrlInformation, bbcHttpCallBuilderService.bbcBuildJourneyStringPathWith(apiTokenAndUrlInformation));
        ResponseEntity<String> result = callService.get(url, HttpEntity.EMPTY);
        return bbcMapperService.mapJsonToJourneys(result.getBody());
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteByCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        if (Optional.ofNullable(apiTokenAndUrlInformation.getArrivalCoordinates()).isPresent() || Optional.ofNullable(apiTokenAndUrlInformation.getDepartureCoordinates()).isPresent()) {
            String url = getBbcRequestString(apiTokenAndUrlInformation, bbcHttpCallBuilderService.bbcBuildJourneyCoordinatesPathWith(apiTokenAndUrlInformation));
            ResponseEntity<String> result = callService.get(url, HttpEntity.EMPTY);
            return bbcMapperService.mapJsonToJourneys(result.getBody());
        }
        return new HashMap<>();
    }

    private String getBbcRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String path) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(path);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}
