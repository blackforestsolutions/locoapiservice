package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpHeadersConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class BritishAirwaysHttpCallBuilderServiceImpl implements BritishAirwaysHttpCallBuilderService {

    private static final String SLASH = "/";
    private static final String DEPARTURE_LOCATION = ";departureLocation=";
    private static final String ARRIVAL_LOCATION = ";arrivalLocation=";
    private static final String SCHEDULED_DEPARTURE_DATE = ";scheduledDepartureDate=";

    @Override
    public URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return HttpCallBuilder.buildUrlWith(apiTokenAndUrlInformation);
    }

    @Override
    public String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "Api version is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "Path Variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat(SLASH)
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(DEPARTURE_LOCATION)
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat(ARRIVAL_LOCATION)
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat(SCHEDULED_DEPARTURE_DATE)
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    @Override
    public HttpHeaders buildHttpHeadersForBritishAirwaysWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = buildBasicHttpHeader();
        setBritishAirwaysApplicationAndClientKeyFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityBritishAirways(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForBritishAirwaysWith(apiTokenAndUrlInformation));
    }

    private HttpHeaders buildBasicHttpHeader() {
        return new HttpHeaders();
    }

    private void setBritishAirwaysApplicationAndClientKeyFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(AdditionalHttpHeadersConfiguration.BA_APPLICATION, AdditionalHttpHeadersConfiguration.BA_APPLICATION_VALUE);
        httpHeaders.add(apiTokenAndUrlInformation.getAuthorizationKey(), apiTokenAndUrlInformation.getAuthorization());
    }

    private String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
