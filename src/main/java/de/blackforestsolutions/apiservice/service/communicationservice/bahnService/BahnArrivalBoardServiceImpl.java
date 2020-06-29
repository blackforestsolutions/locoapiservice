package de.blackforestsolutions.apiservice.service.communicationservice.bahnService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
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

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class BahnArrivalBoardServiceImpl implements BahnArrivalBoardService {

    private final CallService callService;
    private final BahnHttpCallBuilderService bahnArrivalBoardHttpCallBuilderService;

    @Autowired
    public BahnArrivalBoardServiceImpl(CallService callService, BahnHttpCallBuilderService bahnArrivalBoardHttpCallBuilderService) {
        this.callService = callService;
        this.bahnArrivalBoardHttpCallBuilderService = bahnArrivalBoardHttpCallBuilderService;
    }

    public Map<String, ArrivalBoard> getArrivalBoardForRouteFromApiWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) throws Exception {
        String url = getBahnArrivalBoardRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = callService.get(url, bahnArrivalBoardHttpCallBuilderService.buildHttpEntityForBahn(apiTokenAndUrlInformation));
        return map(result.getBody());
    }

    private String getBahnArrivalBoardRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(apiTokenAndUrlInformation);
        builder.setPath(bahnArrivalBoardHttpCallBuilderService.buildBahnArrivalBoardPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
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
