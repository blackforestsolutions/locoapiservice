package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class BritishAirwaysApiServiceImpl implements BritishAirwaysApiService {

    private final CallService callService;
    private final BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService;
    private final BritishAirwaysMapperService britishAirwaysMapperService;

    @Autowired
    public BritishAirwaysApiServiceImpl(CallService callService, BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService, BritishAirwaysMapperService britishAirwaysMapperService) {
        this.callService = callService;
        this.britishAirwaysHttpCallBuilderService = britishAirwaysHttpCallBuilderService;
        this.britishAirwaysMapperService = britishAirwaysMapperService;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String url = getBritishAirwaysRequestString(apiTokenAndUrlInformation);
            ResponseEntity<String> result = callService.getOld(url, britishAirwaysHttpCallBuilderService.buildHttpEntityBritishAirways(apiTokenAndUrlInformation));
            return new CallStatus<>(this.britishAirwaysMapperService.map(result.getBody()), Status.SUCCESS, null);
        } catch (Exception ex) {
            return new CallStatus<>(null, Status.FAILED, ex);
        }
    }

    private String getBritishAirwaysRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(britishAirwaysHttpCallBuilderService.buildPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}




