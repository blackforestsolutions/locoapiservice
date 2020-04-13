package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BahnCallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.DepartureBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BahnDepartureBoardServiceImpl implements BahnDepartureBoardService {

    private final BahnCallService bahnCallService;
    private final BahnHttpCallBuilderService bahnDepartureBoardHttpCallBuilderService;

    @Autowired
    public BahnDepartureBoardServiceImpl(BahnCallService bahnCallService, BahnHttpCallBuilderService bahnDepartureBoardHttpCallBuilderService) {
        this.bahnCallService = bahnCallService;
        this.bahnDepartureBoardHttpCallBuilderService = bahnDepartureBoardHttpCallBuilderService;
    }

    public Map<String, DepartureBoard> getDepartureBoardForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception {
        String url = getBahnDepartureBoardRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = bahnCallService.getRequestAnswer(url, bahnDepartureBoardHttpCallBuilderService.buildHttpEntityForBahn(apiTokenAndUrlInformation));
        return map(result.getBody());
    }

    private String getBahnDepartureBoardRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setApiVersion(apiTokenAndUrlInformation.getApiVersion());
        builder.setGermanRailDepartureBoardPath(apiTokenAndUrlInformation.getGermanRailDepartureBoardPath());
        builder.setStationId(apiTokenAndUrlInformation.getStationId());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setPath(bahnDepartureBoardHttpCallBuilderService.buildBahnDepartureBoardPathWith(builder.build()));
        URL requestUrl = bahnDepartureBoardHttpCallBuilderService.buildBahnUrlWith(builder.build());
        return requestUrl.toString();
    }

    private Map<String, DepartureBoard> map(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<DepartureBoard> departureBoards = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, DepartureBoard.class));
        return mapDepartureBoardListToMap(departureBoards);
    }

    private Map<String, DepartureBoard> mapDepartureBoardListToMap(List<DepartureBoard> departureBoards) {
        Map<String, DepartureBoard> departureBoardMap = new HashMap<>();
        departureBoards
                .forEach(departureBoard -> departureBoardMap.put(departureBoard.getDetailsId(), departureBoard));
        return departureBoardMap;
    }
}
