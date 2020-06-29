package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BBCHttpCallBuilderServiceImpl implements BBCHttpCallBuilderService {

    private static final String API_KEY = "key";
    private static final String FROM_NAME = "fn";
    private static final String TO_NAME = "tn";
    private static final String FROM_COORDINATE = "fc";
    private static final String TO_COORDINATE = "tc";
    private static final String DATE_BEGIN = "db";
    private static final String DATE_END = "de";
    private static final String RADIUS = "radius";
    private static final String LANGUAGE = "locale";
    private static final String CURRENCY = "cur";
    private static final String SEATS = "seats";
    private static final String LIMIT = "limit";
    private static final String SORT_DIRECTION = "sort";

    @Override
    public String bbcBuildJourneyStringPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        return buildBasePathWith(apiTokenAndUrlInformation)
                .concat("&")
                .concat(FROM_NAME)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat(TO_NAME)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getArrival());
    }

    @Override
    public String bbcBuildJourneyCoordinatesPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        return buildBasePathWith(apiTokenAndUrlInformation)
                .concat("&")
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getDepartureCoordinates())
                        .map(dc -> FROM_COORDINATE)
                        .orElse(FROM_NAME)
                )
                .concat("=")
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getDepartureCoordinates())
                        .map(this::convertCoordinatesToRequestParams)
                        .orElse(apiTokenAndUrlInformation.getDeparture())
                )
                .concat("&")
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getArrivalCoordinates())
                        .map(ac -> TO_COORDINATE)
                        .orElse(TO_NAME)
                )
                .concat("=")
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getArrivalCoordinates())
                        .map(this::convertCoordinatesToRequestParams)
                        .orElse(apiTokenAndUrlInformation.getArrival())
                );
    }

    private String buildBasePathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathVariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journeyPathVariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getRadius(), "radius is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getCurrency(), "currency is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getNumberOfPersons(), "numberOfPersons is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLength(), "resultLength is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable())
                .concat("?")
                .concat(resolveArrivalOrDepartureDateWith(apiTokenAndUrlInformation))
                .concat("&")
                .concat(API_KEY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(RADIUS)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getRadius()))
                .concat("&")
                .concat(LANGUAGE)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getLanguage())
                .concat("&")
                .concat(CURRENCY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getCurrency())
                .concat("&")
                .concat(SEATS)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getNumberOfPersons()))
                .concat("&")
                .concat(LIMIT)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getResultLength()));
    }

    private String resolveArrivalOrDepartureDateWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        if (apiTokenAndUrlInformation.getTimeIsDeparture()) {
            Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
            return DATE_BEGIN
                    .concat("=")
                    .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
        }
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrivalDate(), "arrivalDate is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSortDirection(), "sortDirection is not allowed to be null");
        return DATE_END
                .concat("=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getArrivalDate()))
                .concat("&")
                .concat(SORT_DIRECTION)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getSortDirection());
    }

    private String convertCoordinatesToRequestParams(Coordinates coordinates) {
        Objects.requireNonNull(coordinates, "coordinates is not allowed to be null");
        return Double.toString(coordinates.getLatitude())
                .concat("%7C")
                .concat(Double.toString(coordinates.getLongitude()));
    }

    private String transformDateToString(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss"));
    }

}
