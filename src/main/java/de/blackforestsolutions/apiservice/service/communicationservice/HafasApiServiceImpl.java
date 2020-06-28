package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperService;
import de.blackforestsolutions.apiservice.service.mapper.HafasPriceMapper;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.datamodel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Slf4j
@Service
public class HafasApiServiceImpl implements HafasApiService {

    private final CallService callService;
    private final HafasHttpCallBuilderService httpCallBuilderService;
    private final HafasMapperService mapperService;

    @Autowired
    public HafasApiServiceImpl(CallService callService, HafasHttpCallBuilderService httpCallBuilderService, HafasMapperService mapperService) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapperService = mapperService;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, TravelProvider travelProvider, HafasPriceMapper priceMapper) {
        try {
            ResponseEntity<String> result = buildAndExecuteCall(apiTokenAndUrlInformation);
            return new CallStatus<>(mapperService.getJourneysFrom(result.getBody(), travelProvider, priceMapper), Status.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error during calling or mapping HafasApi: ", e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    private ResponseEntity<String> buildAndExecuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws JsonProcessingException {
        String urlDeparture = getHafasRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture());
        String urlArrival = getHafasRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival());
        ResponseEntity<String> departureIdJson = callService.post(urlDeparture, httpCallBuilderService.buildHttpEntityStationForHafas(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        String departureId = mapperService.getIdFrom(departureIdJson.getBody());
        ResponseEntity<String> arrivalIdJson = callService.post(urlArrival, httpCallBuilderService.buildHttpEntityStationForHafas(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        String arrivalId = mapperService.getIdFrom(arrivalIdJson.getBody());
        ApiTokenAndUrlInformation callToken = replaceStartAndDestinationIn(apiTokenAndUrlInformation, departureId, arrivalId);
        String urlJourney = getHafasRequestString(callToken, null);
        return callService.post(urlJourney, httpCallBuilderService.buildHttpEntityJourneyForHafas(callToken));
    }

    private ApiTokenAndUrlInformation replaceStartAndDestinationIn(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String departureId, String arrivalId) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setDeparture(departureId);
        builder.setArrival(arrivalId);
        return builder.build();
    }

    private String getHafasRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setProtocol(apiTokenAndUrlInformation.getProtocol());
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setPath(httpCallBuilderService.buildPathWith(builder.build(), location));
        Optional.ofNullable(apiTokenAndUrlInformation.getChecksum()).ifPresent(builder::setChecksum);
        Optional.ofNullable(apiTokenAndUrlInformation.getMic()).ifPresent(builder::setMic);
        Optional.ofNullable(apiTokenAndUrlInformation.getMac()).ifPresent(builder::setMac);
        Optional.ofNullable(apiTokenAndUrlInformation.getAuthorizationKey()).ifPresent(builder::setAuthorizationKey);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}
