package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Service
public class SearchChHttpCallBuilderServiceImpl implements SearchChHttpCallBuilderService {


    @Override
    public String buildSearchChLocationPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "location path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChTermParameter(), "term parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationSearchTerm(), "search term is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChStationId(), "station parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter(), "coordinate parameter string is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getLocationPath())
                .concat("?")
                .concat(apiTokenAndUrlInformation.getSearchChTermParameter())
                .concat("=")
                .concat(apiTokenAndUrlInformation.getLocationSearchTerm())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChStationId())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter());
    }

    @Override
    public String buildSearchChRoutePath(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChRoutePathVariable(), "route path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure (from) is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getStartLocation(), "start location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival (to) is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDestinationLocation(), "destination location is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDatePathVariable(), "date path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTimePathVariable(), "time path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChDelayParameter(), "delay parameter is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getSearchChResults(), "numbers of results is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getSearchChRoutePathVariable())
                .concat("?")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("=")
                .concat(apiTokenAndUrlInformation.getStartLocation())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("=")
                .concat(apiTokenAndUrlInformation.getDestinationLocation())
                .concat("&")
                .concat(apiTokenAndUrlInformation.getDatePathVariable())
                .concat("=")
                .concat(this.transformDateToString(apiTokenAndUrlInformation.getDepartureDate()))
                .concat("&")
                .concat(apiTokenAndUrlInformation.getTimePathVariable())
                .concat("=")
                .concat(this.transformDateToTimeOfDayString(apiTokenAndUrlInformation.getDepartureDate()))
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
