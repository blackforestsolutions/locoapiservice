package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.BritishAirwaysApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FlightControllerTest {

    private final LufthansaApiService lufthansaApiService = Mockito.mock(LufthansaApiService.class);
    private final BritishAirwaysApiService britishAirwaysApiService = Mockito.mock(BritishAirwaysApiService.class);

    private final FlightController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.flights(testRequest);
        //assert
        Mockito.verify(lufthansaApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(britishAirwaysApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private FlightController initClassUnderTest() {
        FlightController classUnderTest = new FlightController(britishAirwaysApiService, lufthansaApiService);
        classUnderTest.setBritishAirwaysApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl());
        classUnderTest.setLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl());
        return classUnderTest;
    }

}