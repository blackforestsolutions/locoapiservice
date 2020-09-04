package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.OsmMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Slf4j
@Service
public class OSMApiServiceImpl implements OSMApiService {

    private final CallService callService;
    private final OSMHttpCallBuilderService osmHttpCallBuilderService;
    private final OsmMapperService osmMapperService;

    @Autowired
    public OSMApiServiceImpl(CallService callService, OSMHttpCallBuilderService osmHttpCallBuilderService, OsmMapperService osmMapperService) {
        this.callService = callService;
        this.osmHttpCallBuilderService = osmHttpCallBuilderService;
        this.osmMapperService = osmMapperService;
    }

    @Override
    public CallStatus<TravelPointStatus> getTravelPointFrom(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address) {
        try {
            String url = getOSMRequestString(apiTokenAndUrlInformation, address);
            ResponseEntity<String> result = callService.getOld(url, HttpEntity.EMPTY);
            return new CallStatus<>(osmMapperService.mapOsmJsonToTravelPoint(result.getBody()), Status.SUCCESS, null);
        } catch (Exception e) {
            log.error("Unable to get Coordinates for TravelPoint due to : ", e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    private String getOSMRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(osmHttpCallBuilderService.buildOSMPathWith(builder.build(), address));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

}
