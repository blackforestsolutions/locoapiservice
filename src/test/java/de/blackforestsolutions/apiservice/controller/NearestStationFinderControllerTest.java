package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.AirportsFinderApiService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NearestStationFinderControllerTest {

    private final AirportsFinderApiService airportsFinderApiService = Mockito.mock(AirportsFinderApiService.class);

    private final NearestStationFinderController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.retrieveAirportsFinderTravelPoints(testRequest);
        //assert
        Mockito.verify(airportsFinderApiService, Mockito.times(1)).getAirportsWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }


    private NearestStationFinderController initClassUnderTest() {
        NearestStationFinderController classUnderTest = new NearestStationFinderController(airportsFinderApiService);
        classUnderTest.setAirportsFinderApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl());
        return classUnderTest;
    }

}
