package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Objects;

@Service
public class AirportsFinderHttpCallBuilderServiceImpl implements AirportsFinderHttpCallBuilderService {


    @Override
    public URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return HttpCallBuilder.buildUrlWith(apiTokenAndUrlInformation);
    }

    @Override
    public String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String longitude = String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude());
        String latitude = String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude());
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "Path Variable is not allowed to be null");
        Objects.requireNonNull(longitude, "Longitude is not allowed to be null");
        Objects.requireNonNull(latitude, "Latitude is not allowed to be null");

        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("?")
                .concat("radius=")
                .concat("300")
                .concat("&")
                .concat("lng=")
                .concat(longitude)
                .concat("&")
                .concat("lat=")
                .concat(latitude)
                ;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityAirportsFinder(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeaderForAirportsFinderWith(apiTokenAndUrlInformation));
    }

    @Override
    public HttpHeaders buildHttpHeaderForAirportsFinderWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = buildBasicHttpHeader();
        setAirportsFinderClientKeyFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private void setAirportsFinderClientKeyFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(apiTokenAndUrlInformation.getAuthorizationKey(), apiTokenAndUrlInformation.getAuthorization());
    }

    private HttpHeaders buildBasicHttpHeader() {
        return new HttpHeaders();
    }
}
