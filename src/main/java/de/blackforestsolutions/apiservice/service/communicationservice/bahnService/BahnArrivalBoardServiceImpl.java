package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BahnCallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.ArrivalBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BahnArrivalBoardServiceImpl implements BahnArrivalBoardService {

    private final BahnCallService bahnCallService;
    private final BahnHttpCallBuilderService bahnArrivalBoardHttpCallBuilderService;

    @Autowired
    public BahnArrivalBoardServiceImpl(BahnCallService bahnCallService, BahnHttpCallBuilderService bahnArrivalBoardHttpCallBuilderService) {
        this.bahnCallService = bahnCallService;
        this.bahnArrivalBoardHttpCallBuilderService = bahnArrivalBoardHttpCallBuilderService;
    }

    public Map<String, ArrivalBoard> getArrivalBoardForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception {
        String url = getBahnArrivalBoardRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = bahnCallService.getRequestAnswer(url, bahnArrivalBoardHttpCallBuilderService.buildHttpEntityForBahn(apiTokenAndUrlInformation));
        return map(result.getBody());
    }

    private String getBahnArrivalBoardRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setHost(apiTokenAndUrlInformation.getHost());
        builder.setPathVariable(apiTokenAndUrlInformation.getPathVariable());
        builder.setApiVersion(apiTokenAndUrlInformation.getApiVersion());
        builder.setGermanRailArrivalBoardPath(apiTokenAndUrlInformation.getGermanRailArrivalBoardPath());
        builder.setStationId(apiTokenAndUrlInformation.getStationId());
        builder.setGermanRailDatePathVariable(apiTokenAndUrlInformation.getGermanRailDatePathVariable());
        builder.setPath(bahnArrivalBoardHttpCallBuilderService.buildBahnArrivalBoardPathWith(builder.build()));
        URL requestUrl = bahnArrivalBoardHttpCallBuilderService.buildBahnUrlWith(builder.build());
        return requestUrl.toString();
    }

    private Map<String, ArrivalBoard> map(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ArrivalBoard> arrivalBoard = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, ArrivalBoard.class));
        return mapArrivalBoardListToMap(arrivalBoard);
    }

    private Map<String, ArrivalBoard> mapArrivalBoardListToMap(List<ArrivalBoard> arrivalBoards) {
        Map<String, ArrivalBoard> arrivalBoardMap = new HashMap<>();
        arrivalBoards.forEach(arrivalBoard -> arrivalBoardMap.put(arrivalBoard.getDetailsId(), arrivalBoard));
        return arrivalBoardMap;
    }
}
