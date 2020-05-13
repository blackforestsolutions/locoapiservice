package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BahnHttpCallBuilderSeviceImpl extends HttpCallBuilder implements BahnHttpCallBuilderService {

    @Override
    public HttpHeaders buildHttpHeadersForBahnWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setBahnFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityForBahn(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForBahnWith(apiTokenAndUrlInformation));
    }

    private void setBahnFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiTokenAndUrlInformation.getAuthorization());
    }

    @Override
    public String buildBahnRailwayStationPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailLocationPath(), "location path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getBahnLocation(), "location is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getGermanRailLocationPath())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getBahnLocation());
    }

    @Override
    public String buildBahnArrivalBoardPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailArrivalBoardPath(), "arrival Board path is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getStationId(), "stationID is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailDatePathVariable(), "german rail date path varaible is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getGermanRailArrivalBoardPath())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getStationId())
                .concat("?date=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getGermanRailDatePathVariable()));
    }

    @Override
    public String buildBahnDepartureBoardPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailDepartureBoardPath(), "departure Board is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getStationId(), "station Id is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "Departure Date is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getGermanRailDepartureBoardPath())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getStationId())
                .concat("?date=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    @Override
    public String buildBahnJourneyDetailsPath(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvaraible is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiVersion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath(), "journey details path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyDetailsId(), "journey Details are not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath()
                        .concat("/")
                        .concat(apiTokenAndUrlInformation.getJourneyDetailsId().replaceAll("%", "%25")));
    }
}
