package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
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
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String url = getBbcRequestString(apiTokenAndUrlInformation, bbcHttpCallBuilderService.bbcBuildJourneyStringPathWith(apiTokenAndUrlInformation));
            ResponseEntity<String> result = callService.get(url, HttpEntity.EMPTY);
            return new CallStatus<>(bbcMapperService.mapJsonToJourneys(result.getBody()), Status.SUCCESS, null);
        }catch (Exception e) {
            log.error("Error doing calling BBC Api with String: ", e);
            return new CallStatus<>(null, Status.FAILED, e);

    }
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteByCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            if (Optional.ofNullable(apiTokenAndUrlInformation.getArrivalCoordinates()).isPresent() || Optional.ofNullable(apiTokenAndUrlInformation.getDepartureCoordinates()).isPresent()) {
                String url = getBbcRequestString(apiTokenAndUrlInformation, bbcHttpCallBuilderService.bbcBuildJourneyCoordinatesPathWith(apiTokenAndUrlInformation));
                ResponseEntity<String> result = callService.get(url, HttpEntity.EMPTY);
                return new CallStatus<>(bbcMapperService.mapJsonToJourneys(result.getBody()), Status.SUCCESS, null);
            }
            return new CallStatus<>(null, Status.FAILED, new Exception("No coordinates found doing calling BBC Api:"));
        } catch (Exception e) {
            log.error("Error doing calling BBC Api with Coordinates: ", e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }
    private String getBbcRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String path) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(path);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}
