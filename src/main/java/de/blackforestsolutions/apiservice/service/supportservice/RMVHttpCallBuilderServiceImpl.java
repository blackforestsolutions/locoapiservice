package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;

@Service
public class RMVHttpCallBuilderServiceImpl implements RMVHttpCallBuilderService {

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityStationForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForRMVStationWith(apiTokenAndUrlInformation));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityTripForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForRMVTripWith(apiTokenAndUrlInformation));
    }

    @Override
    public String buildLocationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "path is not allowed to be null");
        Objects.requireNonNull(location, "location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "location is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getLocationPath())
                .concat(AdditionalHttpConfiguration.INPUT)
                .concat("=")
                .concat(location)
                .concat("&")
                .concat(AdditionalHttpConfiguration.ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization());
    }

    @Override
    public String buildTripPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath(), "path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath())
                .concat(AdditionalHttpConfiguration.ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(AdditionalHttpConfiguration.ORIGIN_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat(AdditionalHttpConfiguration.DEST_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getArrival());
    }

    private void setRMVAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(AdditionalHttpConfiguration.ACCESS_ID, apiTokenAndUrlInformation.getAuthorization());
    }

    private HttpHeaders buildHttpHeadersForRMVTripWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setRMVAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        setRMVOriginAndDestFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private void setRMVOriginAndDestFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(AdditionalHttpConfiguration.ORIGIN_ID, apiTokenAndUrlInformation.getDeparture());
        httpHeaders.add(AdditionalHttpConfiguration.DEST_ID, apiTokenAndUrlInformation.getArrival());
    }

    private HttpHeaders buildHttpHeadersForRMVStationWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setRMVAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }
}
