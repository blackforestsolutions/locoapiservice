package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallService;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperService;
import de.blackforestsolutions.apiservice.service.mapper.HafasPriceMapper;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class HafasApiServiceImpl implements HafasApiService {

    private final HafasCallService hafasCallService;
    private final HafasHttpCallBuilderService httpCallBuilderService;
    private final HafasMapperService mapperService;

    @Autowired
    public HafasApiServiceImpl(HafasCallService hafasCallService, HafasHttpCallBuilderService httpCallBuilderService, HafasMapperService mapperService) {
        this.hafasCallService = hafasCallService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapperService = mapperService;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, TravelProvider travelProvider, HafasPriceMapper priceMapper) {
        ResponseEntity<String> result = buildAndExecuteCall(apiTokenAndUrlInformation);
        return mapperService.getJourneysFrom(result.getBody(), travelProvider, priceMapper);
    }

    private ResponseEntity<String> buildAndExecuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String urlDeparture = getHafasRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture());
        String urlArrival = getHafasRequestString(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival());
        ResponseEntity<String> departureIdJson = hafasCallService.getStationId(urlDeparture, httpCallBuilderService.buildHttpEntityStationForHafas(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getDeparture()));
        String departureId = (String) mapperService.getIdFrom(departureIdJson.getBody()).getCalledObject();
        ResponseEntity<String> arrivalIdJson = hafasCallService.getStationId(urlArrival, httpCallBuilderService.buildHttpEntityStationForHafas(apiTokenAndUrlInformation, apiTokenAndUrlInformation.getArrival()));
        String arrivalId = (String) mapperService.getIdFrom(arrivalIdJson.getBody()).getCalledObject();
        ApiTokenAndUrlInformation callToken = replaceStartAndDestinationIn(apiTokenAndUrlInformation, departureId, arrivalId);
        String urlJourney = getHafasRequestString(callToken, null);
        return hafasCallService.getJourney(urlJourney, httpCallBuilderService.buildHttpEntityJourneyForHafas(callToken));
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
        if (Optional.ofNullable(apiTokenAndUrlInformation.getChecksum()).isPresent()) {
            builder.setChecksum(apiTokenAndUrlInformation.getChecksum());
        }
        if (Optional.ofNullable(apiTokenAndUrlInformation.getMic()).isPresent()) {
            builder.setMic(apiTokenAndUrlInformation.getMic());
        }
        if (Optional.ofNullable(apiTokenAndUrlInformation.getMac()).isPresent()) {
            builder.setMac(apiTokenAndUrlInformation.getMac());
        }
        if (Optional.ofNullable(apiTokenAndUrlInformation.getAuthorizationKey()).isPresent()) {
            builder.setAuthorization(apiTokenAndUrlInformation.getAuthorizationKey());
        }
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setPath(httpCallBuilderService.buildPathWith(builder.build(), location));
        URL requestUrl = httpCallBuilderService.buildHafasUrlWith(builder.build());
        return requestUrl.toString();
    }
}
