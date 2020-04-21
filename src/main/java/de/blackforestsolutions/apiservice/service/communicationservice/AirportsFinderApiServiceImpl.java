package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.AirportsFinderCallService;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Set;

@Service
@Slf4j
public class AirportsFinderApiServiceImpl implements AirportsFinderApiService {
    private final AirportsFinderCallService airportsFinderCallService;
    private final AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService;
    private final AirportsFinderMapperService airportsFinderMapperService;


    @Autowired
    public AirportsFinderApiServiceImpl(AirportsFinderCallService airportsFinderCallService, AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService, AirportsFinderMapperService airportsFinderMapperService) {
        this.airportsFinderCallService = airportsFinderCallService;
        this.airportsFinderHttpCallBuilderService = airportsFinderHttpCallBuilderService;
        this.airportsFinderMapperService = airportsFinderMapperService;
    }

    @Override
    public Set<CallStatus> getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String url = getAirportsFinderRequestString(apiTokenAndUrlInformation);
        ResponseEntity<String> result = airportsFinderCallService.getNearestAirports(url, airportsFinderHttpCallBuilderService.buildHttpEntityAirportsFinder(apiTokenAndUrlInformation));
        return this.airportsFinderMapperService.map(result.getBody());
    }


    private String getAirportsFinderRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        builder.setDepartureCoordinates(getBuilderDepartureCoordinates(apiTokenAndUrlInformation));
        builder.setPath(airportsFinderHttpCallBuilderService.buildPathWith(builder.build()));
        URL requestUrl = airportsFinderHttpCallBuilderService.buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private Coordinates getBuilderDepartureCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        departureCoordinates.setLatitude(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude());
        departureCoordinates.setLongitude(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude());
        return departureCoordinates.build();
    }
}
