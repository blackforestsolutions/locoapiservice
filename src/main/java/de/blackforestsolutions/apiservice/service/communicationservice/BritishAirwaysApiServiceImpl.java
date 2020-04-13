package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BritishAirwaysCallService;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BritishAirwaysApiServiceImpl implements BritishAirwaysApiService {

    private final BritishAirwaysCallService britishAirwaysCallService;
    private final BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService;
    private final BritishAirwaysMapperService britishAirwaysMapperService;

    @Autowired
    public BritishAirwaysApiServiceImpl(BritishAirwaysCallService britishAirwaysCallService, BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService, BritishAirwaysMapperService britishAirwaysMapperService) {
        this.britishAirwaysCallService = britishAirwaysCallService;
        this.britishAirwaysHttpCallBuilderService = britishAirwaysHttpCallBuilderService;
        this.britishAirwaysMapperService = britishAirwaysMapperService;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getBritishAirwaysRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = britishAirwaysCallService.getFlights(url, britishAirwaysHttpCallBuilderService.buildHttpEntityBritishAirways(apiTokenAndUrlInformation));
        return this.britishAirwaysMapperService.map(result.getBody());
    }

    private String getBritishAirwaysRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(apiTokenAndUrlInformation.getDepartureDate());
        builder.setPath(britishAirwaysHttpCallBuilderService.buildPathWith(builder.build()));
        URL requestUrl = britishAirwaysHttpCallBuilderService.buildUrlWith(builder.build());
        return requestUrl.toString();
    }
}




