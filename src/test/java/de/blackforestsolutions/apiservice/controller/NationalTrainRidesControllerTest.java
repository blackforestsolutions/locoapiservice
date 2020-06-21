package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.BahnJourneyDetailsService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NationalTrainRidesControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final BahnJourneyDetailsService bahnJourneyDetailsService = Mockito.mock(BahnJourneyDetailsService.class);
    private final DBApiService dbApiService = Mockito.mock(DBApiService.class);

    private final NationalTrainRidesController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws JsonProcessingException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.retrieveTrainJourneys(testRequestString);
        //assert
        Mockito.verify(bahnJourneyDetailsService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        Mockito.verify(dbApiService, Mockito.times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private NationalTrainRidesController initClassUnderTest() {
        NationalTrainRidesController classUnderTest = new NationalTrainRidesController(bahnJourneyDetailsService, dbApiService);
        classUnderTest.setBahnApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl());
        classUnderTest.setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getDBTokenAndUrl("", ""));
        return classUnderTest;
    }
}