package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BBCCallService;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BBCApiServiceImpl implements BBCApiService {

    private static final String FN = "fn";
    private static final String QUESTION_MARK = "?";
    private static final String TN = "tn";
    private static final String EQUAL = "=";
    private static final String AND = "&";
    private static final String DB = "db";

    private final BBCCallService bbcCallService;
    private final BBCHttpCallBuilderService bbcHttpCallBuilderService;
    private final BBCMapperService bbcMapperService;

    @Autowired
    public BBCApiServiceImpl(BBCCallService bbcCallService, BBCHttpCallBuilderService bbcHttpCallBuilderService, BBCMapperService bbcMapperService) {
        this.bbcCallService = bbcCallService;
        this.bbcHttpCallBuilderService = bbcHttpCallBuilderService;
        this.bbcMapperService = bbcMapperService;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getBbcRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = bbcCallService.getRide(url, bbcHttpCallBuilderService.buildHttpEntityForBbc(apiTokenAndUrlInformation));
        return bbcMapperService.map(result.getBody());
    }

    private String getBbcRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setPath(bbcHttpCallBuilderService.bbcBuildPathWith(builder.build()));
        URL requestUrl = bbcHttpCallBuilderService.buildUrlWith(builder.build());
        return requestUrl.toString().concat(addVariables(apiTokenAndUrlInformation));
    }

    private String addVariables(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return QUESTION_MARK.concat(FN).concat(EQUAL).concat(apiTokenAndUrlInformation.getDeparture())
                .concat(AND).concat(TN).concat(EQUAL).concat(apiTokenAndUrlInformation.getArrival())
                .concat(AND).concat(DB).concat(EQUAL).concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    private String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
