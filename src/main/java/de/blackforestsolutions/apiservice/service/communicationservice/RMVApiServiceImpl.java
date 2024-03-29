package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BoxService;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Box;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Slf4j
@Service
public class RMVApiServiceImpl implements RMVApiService {

    private final CallService callService;
    private final RMVHttpCallBuilderService httpCallBuilderService;
    private final RMVMapperService rmvMapperService;
    private final BoxService boxService;
    private final Box rmvBox;

    @Autowired
    public RMVApiServiceImpl(CallService callService, RMVHttpCallBuilderService httpCallBuilderService, RMVMapperService rmvMapperService, BoxService boxService, Box rmvBox) {
        this.callService = callService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.rmvMapperService = rmvMapperService;
        this.boxService = boxService;
        this.rmvBox = rmvBox;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteByCoordinatesWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        if (boxService.isCoordinateInBox(apiTokenAndUrlInformation.getDepartureCoordinates(), rmvBox) && boxService.isCoordinateInBox(apiTokenAndUrlInformation.getArrivalCoordinates(), rmvBox)) {
            try {
                ResponseEntity<String> result = buildAndExecteCallForCoordinates(apiTokenAndUrlInformation);
                return new CallStatus<>(rmvMapperService.getJourneysFrom(result.getBody()), Status.SUCCESS, null);
            } catch (Exception e) {
                log.error("Error during calling rmv api: ", e);
                return new CallStatus<>(null, Status.FAILED, e);
            }
        }
        return new CallStatus<>(Collections.emptyMap(), Status.SUCCESS, null);
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteBySearchStringWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            ResponseEntity<String> result = buildAndExecuteCallForSearchString(apiTokenAndUrlInformation);
            return new CallStatus<>(rmvMapperService.getJourneysFrom(result.getBody()), Status.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error during calling rmv api: ", e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    private ResponseEntity<String> buildAndExecteCallForCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws JAXBException {
        String urlDeparture = getRMVRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildLocationCoordinatesPathWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getDepartureCoordinates()
        ));
        String urlArrival = getRMVRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildLocationCoordinatesPathWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getArrivalCoordinates()
        ));
        return buildAndExecuteCall(apiTokenAndUrlInformation, urlDeparture, urlArrival);
    }

    private ResponseEntity<String> buildAndExecuteCallForSearchString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws JAXBException {
        String urlDeparture = getRMVRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildLocationStringPathWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getDeparture()
        ));
        String urlArrival = getRMVRequestString(apiTokenAndUrlInformation, httpCallBuilderService.buildLocationStringPathWith(
                apiTokenAndUrlInformation,
                apiTokenAndUrlInformation.getArrival()
        ));
        return buildAndExecuteCall(apiTokenAndUrlInformation, urlDeparture, urlArrival);
    }

    private ResponseEntity<String> buildAndExecuteCall(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String urlDeparture, String urlArrival) throws JAXBException {
        ResponseEntity<String> departureIdJson = callService.getOld(
                urlDeparture,
                httpCallBuilderService.buildHttpEntityForRMV(apiTokenAndUrlInformation)
        );
        String departureId = rmvMapperService.getIdFrom(departureIdJson.getBody());
        ResponseEntity<String> arrivalIdJson = callService.getOld(
                urlArrival,
                httpCallBuilderService.buildHttpEntityForRMV(apiTokenAndUrlInformation)
        );
        String arrivalId = rmvMapperService.getIdFrom(arrivalIdJson.getBody());
        ApiTokenAndUrlInformation callToken = replaceStartAndDestinationIn(apiTokenAndUrlInformation, departureId, arrivalId);
        String urlTrip = getRMVRequestString(callToken, httpCallBuilderService.buildTripPathWith(callToken));
        return callService.getOld(urlTrip, httpCallBuilderService.buildHttpEntityForRMV(callToken));
    }

    private ApiTokenAndUrlInformation replaceStartAndDestinationIn(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String departureId, String arrivalId) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setDeparture(departureId);
        builder.setArrival(arrivalId);
        return builder.build();
    }

    private String getRMVRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String path) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(path);
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}
