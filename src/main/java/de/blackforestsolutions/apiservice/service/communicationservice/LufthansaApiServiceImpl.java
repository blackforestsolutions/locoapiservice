package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.util.TimeUtil.transformToYyyyMMDdWith;


@Service
public class LufthansaApiServiceImpl implements LufthansaApiService {

    private final CallService callService;
    private final LuftHansaHttpCallBuilderService httpCallBuilderService;
    private final LufthansaMapperService mapper;

    @Autowired
    public LufthansaApiServiceImpl(CallService callService, LuftHansaHttpCallBuilderService httpCallBuilderService, LufthansaMapperService mapper) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapper = mapper;
    }

    @Override
    public CallStatus getLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getLufthansaAuthorizationRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = callService.post(url, httpCallBuilderService.buildHttpEntityForLufthansaAuthorization(apiTokenAndUrlInformation));
        return mapper.mapToAuthorization(result.getBody());
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getLufthansaJourneyRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = callService.get(url, httpCallBuilderService.buildHttpEntityForLufthansaJourney(apiTokenAndUrlInformation));
        return mapper.map(result.getBody());
    }

    private String getLufthansaJourneyRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setApiVersion(apiTokenAndUrlInformation.getApiVersion());
        builder.setJourneyPathVariable(apiTokenAndUrlInformation.getJourneyPathVariable());
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(transformToYyyyMMDdWith(apiTokenAndUrlInformation.getDepartureDate()));
        builder.setAuthorization(apiTokenAndUrlInformation.getAuthorization());
        builder.setXOriginationIp(apiTokenAndUrlInformation.getXOriginationIp());
        builder.setPath(httpCallBuilderService.buildLufthansaJourneyPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getLufthansaAuthorizationRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setApiVersion(apiTokenAndUrlInformation.getApiVersion());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setClientId(apiTokenAndUrlInformation.getClientId());
        builder.setClientSecret(apiTokenAndUrlInformation.getClientSecrect());
        builder.setClientType(apiTokenAndUrlInformation.getClientType());
        builder.setPath(httpCallBuilderService.buildLufthansaAuthorizationPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}