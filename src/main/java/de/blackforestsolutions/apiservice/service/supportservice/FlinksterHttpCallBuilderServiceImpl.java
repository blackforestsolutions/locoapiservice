package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class FlinksterHttpCallBuilderServiceImpl implements FlinksterHttpCallBuilderService {

    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String RADIUS = "radius";
    private static final String PROVIDER_NETWORK = "providernetwork";
    private static final String START = "begin";
    private static final String JSON_EXTENSION = "expand";
    private static final String RENTALOBJECT_EXTENSION = "rentalobject";
    private static final String AREA_EXTENSION = "area";
    private static final String PRICE_EXTENSION = "price";

    @Override
    public URL buildFlinksterUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return buildUrlWith(apiTokenAndUrlInformation);
    }

    @Override
    public String buildFlinksterPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiversion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude(), "longitude is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude(), "latitude is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDistanceFromTravelPoint(), "radius is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getCliendId(), "providerNetwork is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat("bookingproposals")
                .concat("?")
                .concat(LATITUDE)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude()))
                .concat("&")
                .concat(LONGITUDE)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude()))
                .concat("&")
                .concat(RADIUS)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDistanceFromTravelPoint().toString())
                .concat("&")
                .concat(PROVIDER_NETWORK)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getCliendId())
                .concat("&")
                .concat(START)
                .concat("=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()))
                .concat("&")
                .concat(JSON_EXTENSION)
                .concat("=")
                .concat(RENTALOBJECT_EXTENSION)
                .concat("%2C")
                .concat(AREA_EXTENSION)
                .concat("%2C")
                .concat(PRICE_EXTENSION);
    }


    @Override
    public HttpEntity buildHttpEntityForFlinkster(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(this.buildHttpHeadersForFlinksterWith(apiTokenAndUrlInformation));
    }

    private HttpHeaders buildHttpHeadersForFlinksterWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFlinksterAuthorizationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private void setFlinksterAuthorizationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiTokenAndUrlInformation.getAuthorization());
        httpHeaders.add(HttpHeaders.HOST, apiTokenAndUrlInformation.getHost());
    }


    public static String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return dateFormat.format(date).replaceAll(":", "%3A").replaceAll("\\+", "%2B");
    }

/*    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(),
                ZoneId.systemDefault());
    }*/
}
