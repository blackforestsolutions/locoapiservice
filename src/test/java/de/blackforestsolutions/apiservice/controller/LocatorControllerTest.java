package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LocatorControllerTest {

    private final SearchChApiService searchChApiService = Mockito.mock(SearchChApiService.class);

    private final LocatorController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.retrieveLocatorJourneys(testRequest);
        //assert
        Mockito.verify(searchChApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private LocatorController initClassUnderTest() {
        LocatorController classUnderTest = new LocatorController(searchChApiService);
        classUnderTest.setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getSearchChRouteTokenAndUrl());
        return classUnderTest;
    }
}