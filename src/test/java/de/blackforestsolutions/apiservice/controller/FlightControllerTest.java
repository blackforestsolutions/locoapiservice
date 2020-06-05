package de.blackforestsolutions.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.BritishAirwaysApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class FlightControllerTest {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final LufthansaApiService lufthansaApiService = mock(LufthansaApiService.class);
    private final BritishAirwaysApiService britishAirwaysApiService = mock(BritishAirwaysApiService.class);

    private final FlightController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() throws JsonProcessingException {
        //arrange
        ApiTokenAndUrlInformation testRequest = ApiTokenAndUrlInformationObjectMother.requestInfos();
        String testRequestString = locoJsonMapper.map(testRequest);
        //act
        classUnderTest.flights(testRequestString);
        //assert
        verify(lufthansaApiService, times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
        verify(britishAirwaysApiService, times(1)).getJourneysForRouteWith(Mockito.any(ApiTokenAndUrlInformation.class));
    }

    private FlightController initClassUnderTest() {
        FlightController classUnderTest = new FlightController(britishAirwaysApiService, lufthansaApiService);
        classUnderTest.setBritishAirwaysApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl());
        classUnderTest.setLufthansaApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl());
        return classUnderTest;
    }

}