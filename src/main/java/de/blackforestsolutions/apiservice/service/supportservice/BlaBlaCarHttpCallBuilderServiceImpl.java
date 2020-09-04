package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BlaBlaCarHttpCallBuilderServiceImpl implements BlaBlaCarHttpCallBuilderService {

    private static final String API_KEY = "key";
    private static final String FROM_COORDINATE = "from_coordinate";
    private static final String TO_COORDINATE = "to_coordinate";
    private static final String FROM_COUNTRY = "from_country";
    private static final String TO_COUNTRY = "to_country";
    private static final String DATE_BEGIN = "start_date_local";
    private static final String DATE_END = "end_date_local";
    private static final String RADIUS = "radius_in_meters";
    private static final String LANGUAGE = "locale";
    private static final String CURRENCY = "currency";
    private static final String SEATS = "requested_seats";
    private static final String LIMIT = "count";
    private static final String SORT_DIRECTION = "sort";

    @Override
    public String buildJourneyCoordinatesPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathVariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journeyPathVariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureCoordinates(), "departureCoordinates is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrivalCoordinates(), "arrivalCoordinates is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getCurrency(), "currency is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getRadius(), "radius is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getNumberOfPersons(), "numberOfPersons is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLength(), "resultLength is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSortDirection(), "sortDirection is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getCountry(), "country is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable())
                .concat("?")
                .concat(API_KEY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getAuthorization())
                .concat("&")
                .concat(FROM_COORDINATE)
                .concat("=")
                .concat(convertCoordinatesToRequestParams(apiTokenAndUrlInformation.getDepartureCoordinates()))
                .concat("&")
                .concat(TO_COORDINATE)
                .concat("=")
                .concat(convertCoordinatesToRequestParams(apiTokenAndUrlInformation.getArrivalCoordinates()))
                .concat("&")
                .concat(FROM_COUNTRY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getCountry())
                .concat("&")
                .concat(TO_COUNTRY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getCountry())
                .concat("&")
                .concat(LANGUAGE)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getLanguage())
                .concat("&")
                .concat(CURRENCY)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getCurrency())
                .concat("&")
                .concat(DATE_BEGIN)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDepartureDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .concat(addOptionalArrivalDate(apiTokenAndUrlInformation))
                .concat("&")
                .concat(RADIUS)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getRadius()))
                .concat("&")
                .concat(SEATS)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getNumberOfPersons()))
                .concat("&")
                .concat(LIMIT)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getResultLength()))
                .concat("&")
                .concat(SORT_DIRECTION)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getSortDirection());
    }

    private String addOptionalArrivalDate(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return Optional.ofNullable(apiTokenAndUrlInformation.getArrivalDate())
                .map(arrivalDate -> "&"
                        .concat(DATE_END)
                        .concat("=")
                        .concat(arrivalDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .orElse("");

    }

    private String convertCoordinatesToRequestParams(Coordinates coordinates) {
        return Double.toString(coordinates.getLatitude())
                .concat(",")
                .concat(Double.toString(coordinates.getLongitude()));
    }

}
