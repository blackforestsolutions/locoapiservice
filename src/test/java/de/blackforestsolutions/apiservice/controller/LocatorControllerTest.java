package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.SearchChApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

class LocatorControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final SearchChApiService searchChApiService = Mockito.mock(SearchChApiService.class);

    private final LocatorController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws IOException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveLocatorJourneys(testRequestString);
        //assert
        Mockito.verify(searchChApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private LocatorController initClassUnderTest() {
        LocatorController classUnderTest = new LocatorController(searchChApiService);
        classUnderTest.setSearchApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl());
        return classUnderTest;
    }
}