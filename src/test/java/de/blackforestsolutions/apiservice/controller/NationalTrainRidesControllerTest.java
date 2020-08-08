package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.mock;

class NationalTrainRidesControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BahnJourneyDetailsService bahnJourneyDetailsService = Mockito.mock(BahnJourneyDetailsService.class);
    private final ExceptionHandlerService exceptionHandlerService = mock(ExceptionHandlerService.class);

    private final NationalTrainRidesController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws IOException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveTrainJourneys(testRequestString);
        //assert
        Mockito.verify(bahnJourneyDetailsService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private NationalTrainRidesController initClassUnderTest() {
        NationalTrainRidesController classUnderTest = new NationalTrainRidesController(bahnJourneyDetailsService, exceptionHandlerService);
        classUnderTest.setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl());
        return classUnderTest;
    }
}