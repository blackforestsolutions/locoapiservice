package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.datamodel.exception.NoExternalResultFoundException;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getRosenhofHvvStation;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStadthausbrueckeHvvStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HvvApiServiceTest {

    private final CallService callService = mock(CallService.class);

    private final HvvHttpCallBuilderService hvvHttpCallBuilderService = mock(HvvHttpCallBuilderService.class);

    private final HvvMapperService mapperService = mock(HvvMapperService.class);

    @InjectMocks
    private HvvApiService classUnderTest = new HvvApiServiceImpl(callService, hvvHttpCallBuilderService, mapperService);

    @BeforeEach
    void init() throws JsonProcessingException, NoExternalResultFoundException {
        when(hvvHttpCallBuilderService.buildTravelPointPathWith(any(ApiTokenAndUrlInformation.class))).thenReturn("");

        when(hvvHttpCallBuilderService.buildTravelPointHttpEntityForHvv(any(ApiTokenAndUrlInformation.class), anyString()))
                .thenReturn(new HttpEntity<>(""));

        when(hvvHttpCallBuilderService.buildJourneyPathWith(any(ApiTokenAndUrlInformation.class))).thenReturn("/journey");

        when(hvvHttpCallBuilderService.buildJourneyHttpEntityForHvv(any(ApiTokenAndUrlInformation.class), any(HvvStation.class), any(HvvStation.class)))
                .thenReturn(new HttpEntity<>(""));

        when(callService.post(anyString(), any(HttpEntity.class)))
                .thenReturn(new ResponseEntity<>("departure", HttpStatus.OK))
                .thenReturn(new ResponseEntity<>("destination", HttpStatus.OK))
                .thenReturn(new ResponseEntity<>("journey", HttpStatus.OK));

        Journey mockedJourney = getGustavHeinemannStreetToUniversityJourney();
        when(mapperService.getJourneyMapFrom(anyString())).thenReturn(Collections.singletonMap(
                    mockedJourney.getId(),
                    new JourneyStatus(Optional.of(mockedJourney), Optional.empty())
                )
        );

        when(mapperService.getHvvStationFrom(anyString())).thenReturn(
                getRosenhofHvvStation(),
                getStadthausbrueckeHvvStation()
        );
    }

    @Test
    void test_getJourneysForRouteWith_mocked_services_is_excuted_correctly_in_order_and_passes_right_arguments_for_services() throws NoExternalResultFoundException, JsonProcessingException {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        ArgumentCaptor<String> travelPointUrlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> departureArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> departureJsonArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arrivalArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationJsonArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HvvStation> departureStationArg = ArgumentCaptor.forClass(HvvStation.class);
        ArgumentCaptor<HvvStation> arrivalStationArg = ArgumentCaptor.forClass(HvvStation.class);
        ArgumentCaptor<String> journeyUrlArg = ArgumentCaptor.forClass(String.class);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        InOrder inOrder = inOrder(hvvHttpCallBuilderService, callService, mapperService);
        inOrder.verify(hvvHttpCallBuilderService, times(1)).buildTravelPointPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(hvvHttpCallBuilderService, times(1)).buildTravelPointHttpEntityForHvv(any(ApiTokenAndUrlInformation.class), departureArg.capture());
        inOrder.verify(callService, times(1)).post(travelPointUrlArg.capture(), any(HttpEntity.class));
        inOrder.verify(mapperService, times(1)).getHvvStationFrom(departureJsonArg.capture());
        inOrder.verify(hvvHttpCallBuilderService, times(1)).buildTravelPointHttpEntityForHvv(any(ApiTokenAndUrlInformation.class), arrivalArg.capture());
        inOrder.verify(callService, times(1)).post(travelPointUrlArg.capture(), any(HttpEntity.class));
        inOrder.verify(mapperService, times(1)).getHvvStationFrom(destinationJsonArg.capture());
        inOrder.verify(hvvHttpCallBuilderService, times(1)).buildJourneyPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(hvvHttpCallBuilderService, times(1)).buildJourneyHttpEntityForHvv(any(ApiTokenAndUrlInformation.class), departureStationArg.capture(), arrivalStationArg.capture());
        inOrder.verify(callService, times(1)).post(journeyUrlArg.capture(), any(HttpEntity.class));
        assertThat(departureArg.getValue()).isEqualTo(testData.getDeparture());
        assertThat(departureJsonArg.getValue()).isEqualTo("departure");
        assertThat(arrivalArg.getValue()).isEqualTo(testData.getArrival());
        assertThat(travelPointUrlArg.getAllValues()).isEqualTo(List.of("http://api-test.geofox.de", "http://api-test.geofox.de"));
        assertThat(destinationJsonArg.getValue()).isEqualTo("destination");
        assertThat(departureStationArg.getValue()).isEqualToIgnoringGivenFields(getRosenhofHvvStation(), "coordinate");
        assertThat(arrivalStationArg.getValue()).isEqualToIgnoringGivenFields(getStadthausbrueckeHvvStation(), "coordinate");
        assertThat(journeyUrlArg.getValue()).isEqualTo("http://api-test.geofox.de/journey");
    }



    @Test
    void test_getJourneysForRouteFromHvvApiWith_with_mocked_services_returns_same_size_of_journeys() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getHvvTokenAndUrl();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation).getCalledObject();

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        when(callService.post(anyString(), any(HttpEntity.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        doThrow(new RuntimeException()).when(callService).post(anyString(), any(HttpEntity.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_returns_empty_map_when_noExternalResultFoundException_is_thrown_by_mapperservice() throws NoExternalResultFoundException, JsonProcessingException {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        doThrow(new NoExternalResultFoundException()).when(mapperService).getHvvStationFrom(anyString());

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getCalledObject()).isEqualTo(Collections.EMPTY_MAP);
    }

}
