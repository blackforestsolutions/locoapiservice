package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
@Slf4j
public class AirportsFinderApiServiceImpl implements AirportsFinderApiService {
    private final CallService callService;
    private final AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService;
    private final AirportsFinderMapperService airportsFinderMapperService;


    @Autowired
    public AirportsFinderApiServiceImpl(CallService callService, AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService, AirportsFinderMapperService airportsFinderMapperService) {
        this.callService = callService;
        this.airportsFinderHttpCallBuilderService = airportsFinderHttpCallBuilderService;
        this.airportsFinderMapperService = airportsFinderMapperService;
    }


    @Override
    public CallStatus getAirportsWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            String url = getAirportsFinderRequestString(apiTokenAndUrlInformation);

            ResponseEntity<String> result = callService.get(url, airportsFinderHttpCallBuilderService.buildHttpEntityAirportsFinder(apiTokenAndUrlInformation));
            return new CallStatus(this.airportsFinderMapperService.map(result.getBody()), Status.SUCCESS, null);
        } catch (Exception ex) {
            return new CallStatus(null, Status.FAILED, ex);
        }
    }


    private String getAirportsFinderRequestString(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        builder.setDepartureCoordinates(getBuilderDepartureCoordinates(apiTokenAndUrlInformation));
        builder.setPath(airportsFinderHttpCallBuilderService.buildPathWith(builder.build()));
        URL requestUrl = buildUrlWith(builder.build());
        return requestUrl.toString();
    }

    private Coordinates getBuilderDepartureCoordinates(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        departureCoordinates.setLatitude(apiTokenAndUrlInformation.getDepartureCoordinates().getLatitude());
        departureCoordinates.setLongitude(apiTokenAndUrlInformation.getDepartureCoordinates().getLongitude());
        return departureCoordinates.build();
    }
}
