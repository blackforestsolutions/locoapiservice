package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.bahn.RailwayStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class BahnRailwayStationServiceImpl implements BahnRailwayStationService {

    private final CallService callService;
    private final BahnHttpCallBuilderService bahnRailwayStationHttpCallBuilderService;

    @Autowired
    public BahnRailwayStationServiceImpl(CallService callService, BahnHttpCallBuilderService bahnRailwayStationHttpCallBuilderService) {
        this.callService = callService;
        this.bahnRailwayStationHttpCallBuilderService = bahnRailwayStationHttpCallBuilderService;
    }

    @Override
    public Map<String, TravelPoint> getTravelPointsForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) throws Exception {
        String url = getBahnRailwayStationsRequestString(apiTokenAndUrlInformation, location);
        ResponseEntity<String> result = callService.get(url, bahnRailwayStationHttpCallBuilderService.buildHttpEntityForBahn(apiTokenAndUrlInformation));
        return map(result.getBody());
    }

    private String getBahnRailwayStationsRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String location) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(bahnRailwayStationHttpCallBuilderService.buildBahnRailwayStationPathWith(builder.build(), location));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private Map<String, TravelPoint> map(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<RailwayStation> railwayStations = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, RailwayStation.class));
        return mapRailwayStationsToTravelPointMap(railwayStations);
    }

    private Map<String, TravelPoint> mapRailwayStationsToTravelPointMap(List<RailwayStation> railwayStations) {
        Map<String, TravelPoint> travelPointMap = new HashMap<>();
        for (RailwayStation railwayStation : railwayStations) {
            Optional<RailwayStation> optionalRailwayStation = Optional.ofNullable(railwayStation);
            if (optionalRailwayStation.isPresent()) {
                RailwayStation station = optionalRailwayStation.get();
                TravelPoint travelPoint = buildTravelPointWith(station);
                travelPointMap.put(station.getId(), travelPoint);
            }
        }
        return travelPointMap;
    }

    private TravelPoint buildTravelPointWith(RailwayStation station) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName(station.getName());
        travelPoint.setStationId(station.getId());
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(station.getLat(), station.getLon()).build());
        return travelPoint.build();
    }
}
