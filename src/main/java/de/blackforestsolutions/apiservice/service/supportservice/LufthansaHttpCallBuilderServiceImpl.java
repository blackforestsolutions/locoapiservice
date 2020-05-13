package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.transformDateToString;

@Service
public class LufthansaHttpCallBuilderServiceImpl implements LuftHansaHttpCallBuilderService {

    @Override
    public String buildLufthansaAuthorizationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathVariable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getPathVariable());
    }

    @Override
    public String buildLufthansaJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiversion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journeyPathVariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("/")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity<String> buildHttpEntityForLufthansaJourney(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForLufthansaJourney(apiTokenAndUrlInformation));
    }

    @Override
    public HttpEntity<MultiValueMap<String, String>> buildHttpEntityForLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity(buildHttpBodyForLufthansaAuthorization(apiTokenAndUrlInformation), new HttpHeaders());
    }

    private MultiValueMap<String, String> buildHttpBodyForLufthansaAuthorization(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientId(), "clientId is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientSecrect(), "client secret is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientType(), "client type is not allowed to be null");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(AdditionalHttpConfiguration.CLIENT_ID, apiTokenAndUrlInformation.getClientId());
        body.add(AdditionalHttpConfiguration.CLIENT_SECRET, apiTokenAndUrlInformation.getClientSecrect());
        body.add(AdditionalHttpConfiguration.GRANT_TYPE, apiTokenAndUrlInformation.getClientType());
        return body;
    }

    private HttpHeaders buildHttpHeadersForLufthansaJourney(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setLufthansaAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private void setLufthansaAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getXOriginationIp(), "x-origination ip is not allowed to be null");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiTokenAndUrlInformation.getAuthorization());
        httpHeaders.add(AdditionalHttpConfiguration.X_ORIGINATING_IP, apiTokenAndUrlInformation.getXOriginationIp());
    }


}
