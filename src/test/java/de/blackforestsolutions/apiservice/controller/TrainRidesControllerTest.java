package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TrainRidesControllerTest {

    private final BahnJourneyDetailsService bahnJourneyDetailsService = Mockito.mock(BahnJourneyDetailsService.class);
    private final DBApiService dbApiService = Mockito.mock(DBApiService.class);

    private final TrainRidesController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.retrieveTrainJourneys(testRequest);
        //assert
        Mockito.verify(bahnJourneyDetailsService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(dbApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private TrainRidesController initClassUnderTest() {
        TrainRidesController classUnderTest = new TrainRidesController(bahnJourneyDetailsService, dbApiService);
        classUnderTest.setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl());
        classUnderTest.setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getDBTokenAndUrl("", ""));
        return classUnderTest;
    }
}