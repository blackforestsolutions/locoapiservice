package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Service
public class SearchChHttpCallBuilderServiceImpl implements SearchChHttpCallBuilderService {

    private static final String FROM = "from";
    private static final String TO = "to";

    @Override
    public String buildSearchChLocationPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "location path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChTermParameter(), "term parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChStationId(), "station parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter(), "coordinate parameter string is not allowed to be null");
        Objects.requireNonNull(location, "location is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getLocationPath())
                .concat("?")
                .concat(apiTokenAndUrlInformation.getSearchChTermParameter())
                .concat("=")
                .concat(location)
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChStationId())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter());
    }

    @Override
    public String buildSearchChRoutePath(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "route path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDatePathVariable(), "date path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTimePathVariable(), "time path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChDelayParameter(), "delay parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChResults(), "numbers of results is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable())
                .concat("?")
                .concat(FROM)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat(TO)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getDatePathVariable())
                .concat("=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()))
                .concat("&")
                .concat(apiTokenAndUrlInformation.getTimePathVariable())
                .concat("=")
                .concat(transformDateToTimeOfDayString(apiTokenAndUrlInformation.getDepartureDate()))
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChDelayParameter())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChResults());
    }

    private String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    private String transformDateToTimeOfDayString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
}
