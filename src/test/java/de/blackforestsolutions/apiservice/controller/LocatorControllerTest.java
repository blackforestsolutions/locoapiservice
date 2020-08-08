package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.OSMApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LocatorControllerTest {

    private final OSMApiService osmApiService = mock(OSMApiService.class);
    private final ExceptionHandlerService exceptionHandlerService = mock(ExceptionHandlerService.class);

    private final LocatorController classUnderTest = initClassUnderTest();

    @Test
    void test_if_calls_executed_correctly() {
        String address = "Schaffhausen";
        ArgumentCaptor<String> addressArg = ArgumentCaptor.forClass(String.class);

        classUnderTest.retrieveLocatorTravelPoints(address);

        verify(osmApiService, times(1)).getTravelPointFrom(any(ApiTokenAndUrlInformation.class), addressArg.capture());
        assertThat(addressArg.getValue()).isEqualTo(address);
    }

    private LocatorController initClassUnderTest() {
        LocatorController classUnderTest = new LocatorController(osmApiService, exceptionHandlerService);
        classUnderTest.setOsmApiTokenAndUrlInformation(ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl());
        return classUnderTest;
    }
}