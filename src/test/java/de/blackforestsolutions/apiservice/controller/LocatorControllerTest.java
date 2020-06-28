package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.OSMApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.mock;

class LocatorControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final OSMApiService osmApiService = Mockito.mock(OSMApiService.class);
    private final ExceptionHandlerService exceptionHandlerService = mock(ExceptionHandlerService.class);

    private final LocatorController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws IOException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveLocatorJourneys(testRequestString);
        //assert
        Mockito.verify(osmApiService, Mockito.times(1)).getCoordinatesFromTravelPointWith(Mockito.any(ApiTokenAndUrlInformation.class), Mockito.anyString());
    }

    private LocatorController initClassUnderTest() {
        LocatorController classUnderTest = new LocatorController(osmApiService, exceptionHandlerService);
        classUnderTest.setOsmApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl());
        return classUnderTest;
    }
}