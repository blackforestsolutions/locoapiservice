package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.BBCApiService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RideShareControllerTest {
    private final BBCApiService bbcApiService = Mockito.mock(BBCApiService.class);

    private final RideShareController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.retrieveRideSharingJourneys(testRequest);
        //assert
        Mockito.verify(bbcApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private RideShareController initClassUnderTest() {
        RideShareController classUnderTest = new RideShareController(bbcApiService);
        classUnderTest.setBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl());
        return classUnderTest;
    }
}