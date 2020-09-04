package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBBCTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getFlughafenBerlinToHamburgHbfJourney;
import static de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder.createJourneyStatusWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class BBCApiServiceTest {

    private final BBCMapperService bbcMapperService = mock(BBCMapperService.class);
    private final CallService callService = mock(CallService.class);
    private final BBCHttpCallBuilderService bbcHttpCallBuilderService = mock(BBCHttpCallBuilderService.class);

    private final BBCApiService classUnderTest = new BBCApiServiceImpl(callService, bbcHttpCallBuilderService, bbcMapperService);

    @BeforeEach
    void init() throws JsonProcessingException {
        when(bbcHttpCallBuilderService.bbcBuildJourneyStringPathWith(any(ApiTokenAndUrlInformation.class)))
                .thenReturn("");

        when(bbcHttpCallBuilderService.bbcBuildJourneyCoordinatesPathWith(any(ApiTokenAndUrlInformation.class)))
                .thenReturn("");

        when(bbcMapperService.mapJsonToJourneys(anyString()))
                .thenReturn(Map.of(
                        getFlughafenBerlinToHamburgHbfJourney().getId(), createJourneyStatusWith(getFlughafenBerlinToHamburgHbfJourney()),
                        getBerlinHbfToHamburgLandwehrJourney().getId(), createJourneyStatusWith(getBerlinHbfToHamburgLandwehrJourney())
                ));
    }

    @Test
    void test_getJourneysForRouteWith_executes_apiSerive_in_right_order() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = getBBCTokenAndUrl();
        when(callService.getOld(anyString(), any(HttpEntity.class)))
                .thenReturn(new ResponseEntity<>(getResourceFileAsString("json/bbcTest.json"), HttpStatus.OK));
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        InOrder inOrder = inOrder(bbcMapperService, bbcHttpCallBuilderService, callService);
        inOrder.verify(bbcHttpCallBuilderService, times(1)).bbcBuildJourneyStringPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(callService, times(1)).getOld(url.capture(), httpEntity.capture());
        inOrder.verify(bbcMapperService, times(1)).mapJsonToJourneys(body.capture());
        assertThat(url.getValue()).isEqualTo("https://public-api.blablacar.com");
        assertThat(body.getValue()).isEqualTo(getResourceFileAsString("json/bbcTest.json"));
        assertThat(httpEntity.getValue()).isEqualTo(HttpEntity.EMPTY);
        assertThat(result.getCalledObject().size()).isEqualTo(2);
    }

    @Test
    void test_getJourneysForRouteByCoordinates_executes_apiSerive_in_right_order() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = getBBCTokenAndUrl();
        when(callService.getOld(anyString(), any(HttpEntity.class)))
                .thenReturn(new ResponseEntity<>(getResourceFileAsString("json/bbcTest.json"), HttpStatus.OK));
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinates(testData);

        InOrder inOrder = inOrder(bbcMapperService, bbcHttpCallBuilderService, callService);
        inOrder.verify(bbcHttpCallBuilderService, times(1)).bbcBuildJourneyCoordinatesPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(callService, times(1)).getOld(url.capture(), httpEntity.capture());
        inOrder.verify(bbcMapperService, times(1)).mapJsonToJourneys(body.capture());
        assertThat(url.getValue()).isEqualTo("https://public-api.blablacar.com");
        assertThat(body.getValue()).isEqualTo(getResourceFileAsString("json/bbcTest.json"));
        assertThat(httpEntity.getValue()).isEqualTo(HttpEntity.EMPTY);
        assertThat(result.getCalledObject().size()).isEqualTo(2);
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_arrival_coordintes_as_null_and_departure_coordinates_as_null_returns_empty_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBBCTokenAndUrl());
        testData.setArrivalCoordinates(null);
        testData.setDepartureCoordinates(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBBCTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_with_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBBCTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }
}
