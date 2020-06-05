package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.AirportsFinderApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NearestStationFinderControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final AirportsFinderApiService airportsFinderApiService = Mockito.mock(AirportsFinderApiService.class);

    private final NearestStationFinderController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws JsonProcessingException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveAirportsFinderTravelPoints(testRequestString);
        //assert
        Mockito.verify(airportsFinderApiService, Mockito.times(1)).getAirportsWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }


    private NearestStationFinderController initClassUnderTest() {
        NearestStationFinderController classUnderTest = new NearestStationFinderController(airportsFinderApiService);
        classUnderTest.setAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl());
        return classUnderTest;
    }

}
