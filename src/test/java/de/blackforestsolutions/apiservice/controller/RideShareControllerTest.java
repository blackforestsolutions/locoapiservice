package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.BBCApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RideShareControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BBCApiService bbcApiService = Mockito.mock(BBCApiService.class);

    private final RideShareController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws JsonProcessingException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveRideSharingJourneys(testRequestString);
        //assert
        Mockito.verify(bbcApiService, Mockito.times(1)).getJourneysForRouteByCoordinates(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(bbcApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private RideShareController initClassUnderTest() {
        RideShareController classUnderTest = new RideShareController(bbcApiService);
        classUnderTest.setBbcApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBBCTokenAndUrl());
        return classUnderTest;
    }
}