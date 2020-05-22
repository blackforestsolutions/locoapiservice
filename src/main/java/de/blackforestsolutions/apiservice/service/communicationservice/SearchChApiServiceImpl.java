package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.util.CombinationUtil;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

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
    public Map<String, TravelPoint> getTravelPointForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) throws IOException {
        String url = getTravelPointRequestString(apiTokenAndUrlInformation, station);
        ResponseEntity<String> result = callService.get(url, buildEmptyHttpEntity());
        return searchChMapperService.getTravelPointFrom(result.getBody());
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws IOException {
        Set<TravelPoint> departures = new HashSet<>(getTravelPointForRouteFromApiWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getDeparture()
        ).values());
        Set<TravelPoint> arrivals = new HashSet<>(getTravelPointForRouteFromApiWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getArrival()
        ).values());
        return CombinationUtil.buildCombinationsPairsBy(departures, arrivals)
                .stream()
                .map(pair -> {
                    String url = getRouteRequestString(apiTokenAndUrlInformation, pair.getFirst().getStationId(), pair.getSecond().getStationId());
                    ResponseEntity<String> result = callService.get(url, buildEmptyHttpEntity());
                    String body = nullsaveResponseBodyMapping(result);
                    return searchChMapperService.getJourneysFrom(body);
                })
                .flatMap(results -> results.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    private String nullsaveResponseBodyMapping(ResponseEntity<String> requestAnswer) {
        return Optional.ofNullable(requestAnswer).map(request -> requestAnswer.getBody()).orElseThrow(NullPointerException::new);
    }

}
