package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.LufthansaCallService;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.apiservice.util.TimeUtil;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;


@Service
public class LufthansaApiServiceImpl implements LufthansaApiService {

    private final LufthansaCallService lufthansaCallService;
    private final LuftHansaHttpCallBuilderService httpCallBuilderService;
    private final LufthansaMapperService mapper;

    @Autowired
    public LufthansaApiServiceImpl(LufthansaCallService lufthansaCallService, LuftHansaHttpCallBuilderService httpCallBuilderService, LufthansaMapperService mapper) {
        this.lufthansaCallService = lufthansaCallService;
        this.httpCallBuilderService = httpCallBuilderService;
        this.mapper = mapper;
    }

    @Override
    public Map<UUID, JourneyStatus> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getLufthansaRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = lufthansaCallService.getFlights(url, httpCallBuilderService.buildHttpEntityForLuftHansa(apiTokenAndUrlInformation));
        return mapper.map(result.getBody());
    }

    private String getLufthansaRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        builder.setDeparture(apiTokenAndUrlInformation.getDeparture());
        builder.setArrival(apiTokenAndUrlInformation.getArrival());
        builder.setDepartureDate(TimeUtil.transformToYyyyMMDdWith(apiTokenAndUrlInformation.getDepartureDate()));
        builder.setPath(httpCallBuilderService.buildLuftHansaPathWith(builder.build()));
        URL requestUrl = httpCallBuilderService.buildLuftHansaUrlWith(builder.build());
        return requestUrl.toString();
    }

}