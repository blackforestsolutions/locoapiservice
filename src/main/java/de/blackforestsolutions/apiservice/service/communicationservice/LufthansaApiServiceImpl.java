package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Slf4j
@Service
public class LufthansaApiServiceImpl implements LufthansaApiService {

    private static final int EXPIRATION_TIME_IN_MILLISECONDS = 86400000;
    private static final int MILLISECONDS = 1000;
    private static final double SECURITY_EXPIRATION_TIME = 2 / 3d;

    private final CallService callService;
    private final LuftHansaHttpCallBuilderService httpCallBuilderService;
    private final LufthansaMapperService mapper;

    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    public LufthansaApiServiceImpl(CallService callService, LuftHansaHttpCallBuilderService httpCallBuilderService, LufthansaMapperService mapper) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapper = mapper;
    }

    @Scheduled(fixedRateString = "${lufthansaBearerExpirationTime}")
    public void updateBearerTokenEveryDay() {
        CallStatus<LufthansaAuthorization> callStatus = getLufthansaAuthorization(lufthansaApiTokenAndUrlInformation);
        if (callStatus.getStatus().equals(Status.SUCCESS)) {
            LufthansaAuthorization lufthansaAuthorization = callStatus.getCalledObject();
            String authorization = lufthansaAuthorization.getAccessToken();
            lufthansaApiTokenAndUrlInformation.setAuthorization("Bearer ".concat(authorization));
            log.info("Luthansa Api Token was updated");
            if (lufthansaAuthorization.getExpiresIn() * MILLISECONDS * SECURITY_EXPIRATION_TIME != EXPIRATION_TIME_IN_MILLISECONDS) {
                log.warn("Expiration Time for lufthansa token has changed!");
            }
        } else {
            log.warn("Bearer token could no be updated!");
        }
    }

    private CallStatus<LufthansaAuthorization> getLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getLufthansaAuthorizationRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = callService.post(url, httpCallBuilderService.buildHttpEntityForLufthansaAuthorization(apiTokenAndUrlInformation));
        return mapper.mapToAuthorization(result.getBody());
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String url = getLufthansaJourneyRequestString(apiTokenAndUrlInformation);
            ResponseEntity<String> result = callService.getOld(url, httpCallBuilderService.buildHttpEntityForLufthansaJourney(apiTokenAndUrlInformation));
            return new CallStatus<>(mapper.map(result.getBody()), Status.SUCCESS, null);
        } catch (Exception ex) {
            log.error("Lufthansa api call was not successful", ex);
            return new CallStatus<>(null, Status.FAILED, ex);
        }
    }

    private String getLufthansaJourneyRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(httpCallBuilderService.buildLufthansaJourneyPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getLufthansaAuthorizationRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(httpCallBuilderService.buildLufthansaAuthorizationPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}