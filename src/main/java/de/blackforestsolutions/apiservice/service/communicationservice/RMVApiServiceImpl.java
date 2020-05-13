package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class RMVApiServiceImpl implements RMVApiService {

    private final CallService callService;
    private final RMVHttpCallBuilderService httpCallBuilderService;
    private final RMVMapperService rmvMapperService;

    @Autowired
    public RMVApiServiceImpl(CallService callService, RMVHttpCallBuilderService httpCallBuilderService, RMVMapperService rmvMapperService) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.rmvMapperService = rmvMapperService;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ResponseEntity<String> result = buildAndExecuteCall(apiTokenAndUrlInformation);
        return rmvMapperService.getJourneysFrom(result.getBody());
    }

    private ResponseEntity<String> buildAndExecuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String urlDeparture = getRMVRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture());
        String urlArrival = getRMVRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival());
        ResponseEntity<String> departureIdJson = callService.get(urlDeparture, httpCallBuilderService.buildHttpEntityStationForRMV(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        String departureId = (String) rmvMapperService.getIdFrom(departureIdJson.getBody()).getCalledObject();
        ResponseEntity<String> arrivalIdJson = callService.get(urlArrival, httpCallBuilderService.buildHttpEntityStationForRMV(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        String arrivalId = (String) rmvMapperService.getIdFrom(arrivalIdJson.getBody()).getCalledObject();
        ApiTokenAndUrlInformation callToken = replaceStartAndDestinationIn(apiTokenAndUrlInformation, departureId, arrivalId);
        String urlTrip = getRMVRequestString(callToken, null);
        return callService.get(urlTrip, httpCallBuilderService.buildHttpEntityTripForRMV(callToken));
    }

    private ApiTokenAndUrlInformation replaceStartAndDestinationIn(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String departureId, String arrivalId) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setDeparture(departureId);
        builder.setArrival(arrivalId);
        return builder.build();
    }

    private String getRMVRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        if (location != null) {
            builder.setPath(apiTokenAndUrlInformation.getLocationPath());
        } else {
            builder.setPath(apiTokenAndUrlInformation.getGermanRailJourneyDeatilsPath());
        }
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        handleLocationCaseFor(builder, location);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private void handleLocationCaseFor(ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder, String location) {
        if (location == null) {
            builder.setPath(httpCallBuilderService.buildTripPathWith(builder.build()));
        } else {
            builder.setPath(httpCallBuilderService.buildLocationPathWith(builder.build(), location));
        }
    }

}
