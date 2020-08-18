package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class RMVHttpCallBuilderServiceImpl implements RMVHttpCallBuilderService {

    private static final String ACCESS_ID = "accessId";
    private static final String INPUT = "input";
    private static final String LANGUAGE = "lang";
    private static final String ORIGIN_ID = "originId";
    private static final String DEST_ID = "destId";
    private static final String LATITUDE = "originCoordLat";
    private static final String LONGITUDE = "originCoordLong";
    private static final String RADIUS = "r";
    private static final String STATION_TYPE = "type";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String ARRIVAL_OR_DEPARTURE_DATE = "searchForArrival";
    private static final String RESULTS_BEFORE_DATE = "numB";
    private static final String RESULT_AFTER_DATE = "numF";
    private static final String STOPS = "passlist";
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    @Override
    public HttpEntity<String> buildHttpEntityForRMV(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForRMVStationWith(apiTokenAndUrlInformation));
    }

    @Override
    public String buildLocationStringPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "location path is not allowed to be null");
        Objects.requireNonNull(location, "location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getLocationPath())
                .concat(INPUT)
                .concat("=")
                .concat(location)
                .concat("&")
                .concat(ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(LANGUAGE)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getLanguage());
    }

    @Override
    public String buildLocationCoordinatesPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, Coordinates coordinates) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getCoordinatesPath(), "coordinates path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        Objects.requireNonNull(coordinates, "coordinates is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getRadius(), "radius is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getOutputFormat(), "station types (outputFormat) is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getCoordinatesPath())
                .concat(ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(LATITUDE)
                .concat("=")
                .concat(String.valueOf(coordinates.getLatitude()))
                .concat("&")
                .concat(LONGITUDE)
                .concat("=")
                .concat(String.valueOf(coordinates.getLongitude()))
                .concat("&")
                .concat(RADIUS)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getRadius()))
                .concat("&")
                .concat(STATION_TYPE)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getOutputFormat())
                .concat("&")
                .concat(LANGUAGE)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getLanguage());
    }

    @Override
    public String buildTripPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTimeIsDeparture(), "timeIsDeparture is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLengthBeforeDepartureTime(), "resultLengthBeforeDepartureTime is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime(), "resultLengthAfterDepartureTime is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowIntermediateStops(), "allowIntermediateStops is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable())
                .concat(ACCESS_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(ORIGIN_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat(DEST_ID)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("&")
                .concat(DATE)
                .concat("=")
                .concat(getArrivalOrDepartureDate(apiTokenAndUrlInformation))
                .concat("&")
                .concat(TIME)
                .concat("=")
                .concat(getArrivalOrDepartureTime(apiTokenAndUrlInformation))
                .concat("&")
                .concat(ARRIVAL_OR_DEPARTURE_DATE)
                .concat("=")
                .concat(getTimeIsDeparture(apiTokenAndUrlInformation.getTimeIsDeparture()))
                .concat("&")
                .concat(RESULTS_BEFORE_DATE)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getResultLengthBeforeDepartureTime()))
                .concat("&")
                .concat(RESULT_AFTER_DATE)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime()))
                .concat("&")
                .concat(STOPS)
                .concat("=")
                .concat(getAllowIntermediateStops(apiTokenAndUrlInformation.getAllowIntermediateStops()));
    }

    private String getArrivalOrDepartureDate(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        if (apiTokenAndUrlInformation.getTimeIsDeparture()) {
            Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
            return convertDateToString(apiTokenAndUrlInformation.getDepartureDate());
        }
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrivalDate(), "arrival date is not allowed to be null");
        return convertDateToString(apiTokenAndUrlInformation.getArrivalDate());
    }

    private String getArrivalOrDepartureTime(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        if (apiTokenAndUrlInformation.getTimeIsDeparture()) {
            Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
            return convertTimeToString(apiTokenAndUrlInformation.getDepartureDate());
        }
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrivalDate(), "arrival date is not allowed to be null");
        return convertTimeToString(apiTokenAndUrlInformation.getArrivalDate());
    }

    private String getTimeIsDeparture(boolean criteria) {
        if (criteria) {
            return FALSE;
        }
        return TRUE;
    }

    private String getAllowIntermediateStops(boolean criteria) {
        if (criteria) {
            return TRUE;
        }
        return FALSE;
    }

    private void setRMVAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(ACCESS_ID, apiTokenAndUrlInformation.getAuthorization());
    }

    private HttpHeaders buildHttpHeadersForRMVStationWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setRMVAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    private String convertDateToString(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String convertTimeToString(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
