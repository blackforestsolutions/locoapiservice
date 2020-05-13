package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.HvvApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.NahShApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.RMVApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.VBBApiService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RegionalTrainRidesControllerTest {

    private final HvvApiService hvvApiService = Mockito.mock(HvvApiService.class);
    private final RMVApiService rmvApiService = Mockito.mock(RMVApiService.class);
    private final VBBApiService vbbApiService = Mockito.mock(VBBApiService.class);
    private final NahShApiService nahShApiService = Mockito.mock(NahShApiService.class);
    private final RegionalTrainRidesController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        classUnderTest.retrieveTrainJourneys(testRequest);
        //assert
        Mockito.verify(hvvApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(rmvApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(vbbApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(nahShApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private RegionalTrainRidesController initClassUnderTest() {
        RegionalTrainRidesController classUnderTest = new RegionalTrainRidesController(hvvApiService, rmvApiService, vbbApiService, nahShApiService);
        classUnderTest.setHvvApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getHvvJourneyTokenAndUrl());
        classUnderTest.setRMVApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl("", ""));
        classUnderTest.setVbbApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("", ""));
        classUnderTest.setNahShApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", ""));
        return classUnderTest;
    }
}