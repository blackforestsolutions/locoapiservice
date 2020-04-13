package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpHeadersConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Objects;

@Service
public class RMVHttpCallBuilderServiceImpl extends HttpCallBuilder implements RMVHttpCallBuilderService {

    @Override
    public URL buildRMVUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return buildUrlWith(apiTokenAndUrlInformation);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityStationForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return new HttpEntity<>(buildHttpHeadersForRMVStationWith(apiTokenAndUrlInformation, station));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityTripForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForRMVTripWith(apiTokenAndUrlInformation));
    }

    @Override
    public String buildLocationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(location, "location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "location is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getLocationPath())
                .concat(AdditionalHttpHeadersConfiguration.INPUT)
                .concat("=")
                .concat(location)
                .concat("&")
                .concat(AdditionalHttpHeadersConfiguration.ACCESS_ID)
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
                .concat(AdditionalHttpHeadersConfiguration.ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(AdditionalHttpHeadersConfiguration.ORIGIN_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat(AdditionalHttpHeadersConfiguration.DEST_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getArrival());
    }

    private void setRMVAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(AdditionalHttpHeadersConfiguration.ACCESS_ID, apiTokenAndUrlInformation.getAuthorization());
    }

    private void setRMVStationFor(HttpHeaders httpHeaders, String station) {
        httpHeaders.add(AdditionalHttpHeadersConfiguration.INPUT, station);
    }

    private HttpHeaders buildHttpHeadersForRMVTripWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setRMVAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        setRMVOriginAndDestFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private void setRMVOriginAndDestFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(AdditionalHttpHeadersConfiguration.ORIGIN_ID, apiTokenAndUrlInformation.getDeparture());
        httpHeaders.add(AdditionalHttpHeadersConfiguration.DEST_ID, apiTokenAndUrlInformation.getArrival());
    }

    private HttpHeaders buildHttpHeadersForRMVStationWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setRMVAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        setRMVStationFor(httpHeaders, station);
        return httpHeaders;
    }
}
