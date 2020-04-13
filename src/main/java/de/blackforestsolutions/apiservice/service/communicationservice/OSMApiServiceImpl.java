package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.OSMCallService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.osm.OsmTravelPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;

@Service
public class OSMApiServiceImpl implements OSMApiService {

    private static final int FIRST_INDEX = 0;

    private final OSMCallService osmCallService;
    private final OSMHttpCallBuilderService osmHttpCallBuilderService;

    @Autowired
    public OSMApiServiceImpl(OSMCallService osmCallService, OSMHttpCallBuilderService osmHttpCallBuilderService) {
        this.osmCallService = osmCallService;
        this.osmHttpCallBuilderService = osmHttpCallBuilderService;
    }

    private static Coordinates map(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OsmTravelPoint> osmTravelPointList = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, OsmTravelPoint.class));
        return mapOsmTravelPointToCoordinates(osmTravelPointList.get(FIRST_INDEX));
    }

    private static Coordinates mapOsmTravelPointToCoordinates(OsmTravelPoint osmTravelPoint) {
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLatitude(Double.parseDouble(osmTravelPoint.getLat()));
        coordinates.setLongitude(Double.parseDouble(osmTravelPoint.getLon()));
        return coordinates.build();
    }

    @Override
    public CallStatus getCoordinatesFromTravelPointWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address) {
        String url = getOSMRequestString(apiTokenAndUrlInformation, address);
        ResponseEntity<String> result = osmCallService.getTravelPoints(url, buildEmptyHttpEntity());
        try {
            return new CallStatus(
                    map(result.getBody()),
                    Status.SUCCESS,
                    null
            );
        } catch (JsonProcessingException e) {
            return new CallStatus(
                    null,
                    Status.FAILED,
                    e
            );
        }
    }

    private String getOSMRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(osmHttpCallBuilderService.buildOSMPathWith(builder.build(), address));
        URL requestUrl = osmHttpCallBuilderService.buildOSMUrlWith(builder.build());
        return requestUrl.toString();
    }

}
