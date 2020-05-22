package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AirportsFinderHttpCallBuilderServiceImpl implements AirportsFinderHttpCallBuilderService {

    private static final String QUESTIONMARK = "?";
    private static final String RADIUS = "radius";
    private static final String EQUALS = "=";
    private static final String RADIUS_IN_KM = "300";
    private static final String AND_SYMBOL = "&";
    private static final String LNG = "lng";
    private static final String LAT = "lat";
    private static final String SLASH = "/";

    @Override
    public String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String longitude = String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude());
        String latitude = String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude());
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "Path Variable is not allowed to be null");
        Objects.requireNonNull(longitude, "Longitude is not allowed to be null");
        Objects.requireNonNull(latitude, "Latitude is not allowed to be null");

        return SLASH
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(QUESTIONMARK)
                .concat(RADIUS)
                .concat(EQUALS)
                .concat(RADIUS_IN_KM)
                .concat(AND_SYMBOL)
                .concat(LNG)
                .concat(EQUALS)
                .concat(longitude)
                .concat(AND_SYMBOL)
                .concat(LAT)
                .concat(EQUALS)
                .concat(latitude);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity<String> buildHttpEntityAirportsFinder(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
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
