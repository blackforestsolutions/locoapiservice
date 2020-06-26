package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Slf4j
@Service
public class SearchChApiServiceImpl implements SearchChApiService {

    private final CallService callService;
    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService;
    private final SearchChMapperService searchChMapperService;

    @Autowired
    public SearchChApiServiceImpl(CallService callService, SearchChHttpCallBuilderService searchChHttpCallBuilderService, SearchChMapperService searchChMapperService) {
        this.callService = callService;
        this.searchChHttpCallBuilderService = searchChHttpCallBuilderService;
        this.searchChMapperService = searchChMapperService;
    }


    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String departureId = getIdFromStation(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture());
            String arrivalId = getIdFromStation(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival());

            String url = getRouteRequestString(apiTokenAndUrlInformation, departureId, arrivalId);
            ResponseEntity<String> result = callService.get(url, buildEmptyHttpEntity());
            return new CallStatus<>(searchChMapperService.getJourneysFrom(result.getBody()), Status.SUCCESS, null);

        } catch (Exception ex) {
            log.error("Error during calling SearchCh api", ex);
            return new CallStatus<>(null, Status.FAILED, ex);
        }
    }

    private String getIdFromStation(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) throws IOException {
        String url = getTravelPointRequestString(apiTokenAndUrlInformation, station);
        ResponseEntity<String> result = callService.get(url, buildEmptyHttpEntity());
        return searchChMapperService.getIdFromStation(result.getBody());
    }


    private String getTravelPointRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setLocationPath(apiTokenAndUrlInformation.getLocationPath());
        builder.setSearchChTermParameter(apiTokenAndUrlInformation.getSearchChTermParameter());
        builder.setSearchChStationId(apiTokenAndUrlInformation.getSearchChStationId());
        builder.setSearchChStationCoordinateParameter(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter());
        builder.setPath(searchChHttpCallBuilderService.buildSearchChLocationPath(builder.build(), location));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getRouteRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String departure, String arrival) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPort(apiTokenAndUrlInformation.getPort());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setSearchChRoutePathVariable(apiTokenAndUrlInformation.getSearchChRoutePathVariable());
        builder.setDeparture(departure);
        builder.setStartLocation(apiTokenAndUrlInformation.getStartLocation());
        builder.setArrival(arrival);
        builder.setDestinationLocation(apiTokenAndUrlInformation.getDestinationLocation());
        builder.setDatePathVariable(apiTokenAndUrlInformation.getDatePathVariable());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setTimePathVariable(apiTokenAndUrlInformation.getTimePathVariable());
        builder.setSearchChDelayParameter(apiTokenAndUrlInformation.getSearchChDelayParameter());
        builder.setSearchChResults(apiTokenAndUrlInformation.getSearchChResults());

        builder.setPath(searchChHttpCallBuilderService.buildSearchChRoutePath(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}
