package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.SearchChCallService;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class SearchChApiServiceImpl implements SearchChApiService {

    private final SearchChCallService searchChCallService;
    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService;
    private final SearchChMapperService searchChMapperService;

    @Autowired
    public SearchChApiServiceImpl(SearchChCallService searchChCallService, SearchChHttpCallBuilderService searchChHttpCallBuilderService, SearchChMapperService searchChMapperService) {
        this.searchChCallService = searchChCallService;
        this.searchChHttpCallBuilderService = searchChHttpCallBuilderService;
        this.searchChMapperService = searchChMapperService;
    }

    @Override
    public Map<String, TravelPoint> getTravelPointForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws IOException {
        String url = getTravelPointRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = searchChCallService.getRequestAnswer(url, buildEmptyHttpEntity());
        return searchChMapperService.getTravelPointFrom(result.getBody());
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getRouteRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = searchChCallService.getRequestAnswer(url, buildEmptyHttpEntity());
        String body = nullsaveResponseBodyMapping(result);
        return searchChMapperService.getJourneysFrom(body);
    }

    private String getTravelPointRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setLocationPath(apiTokenAndUrlInformation.getLocationPath());
        builder.setSearchChTermParameter(apiTokenAndUrlInformation.getSearchChTermParameter());
        builder.setLocationSearchTerm(apiTokenAndUrlInformation.getLocationSearchTerm());
        builder.setSearchChStationId(apiTokenAndUrlInformation.getSearchChStationId());
        builder.setSearchChStationCoordinateParameter(apiTokenAndUrlInformation.getSearchChStationCoordinateParameter());
        builder.setPath(searchChHttpCallBuilderService.buildSearchChLocationPath(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private String getRouteRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPort(apiTokenAndUrlInformation.getPort());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setSearchChRoutePathVariable(apiTokenAndUrlInformation.getSearchChRoutePathVariable());
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setStartLocation(apiTokenAndUrlInformation.getStartLocation());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
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

    private String nullsaveResponseBodyMapping(ResponseEntity<String> requestAnswer) {
        return Optional.ofNullable(requestAnswer).map(request -> requestAnswer.getBody()).orElseThrow(NullPointerException::new);
    }

}
