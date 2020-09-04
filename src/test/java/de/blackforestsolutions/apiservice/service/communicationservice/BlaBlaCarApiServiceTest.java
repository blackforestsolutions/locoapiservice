package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonParseException;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.BlaBlaCarMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.BlaBlaCarHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBlaBlaCarApiTokenAndUrl;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class BlaBlaCarApiServiceTest {

    private final BlaBlaCarMapperService blaBlaCarMapperService = mock(BlaBlaCarMapperService.class);
    private final CallService callService = mock(CallService.class);
    private final BlaBlaCarHttpCallBuilderService blaBlaCarHttpCallBuilderService = mock(BlaBlaCarHttpCallBuilderService.class);

    private final BlaBlaCarApiService classUnderTest = new BlaBlaCarApiServiceImpl(callService, blaBlaCarHttpCallBuilderService, blaBlaCarMapperService);

    @BeforeEach
    void init() {
        when(blaBlaCarHttpCallBuilderService.buildJourneyCoordinatesPathWith(any(ApiTokenAndUrlInformation.class)))
                .thenReturn("");

        when(callService.get(anyString(), any(HttpEntity.class)))
                .thenReturn(Mono.just(new ResponseEntity<>(getResourceFileAsString("json/blaBlaCarJourney.json"), HttpStatus.OK)));

        when(blaBlaCarMapperService.buildJourneysWith(any(Rides.class)))
                .thenReturn(Flux.just(
                        new CallStatus<>(JourneyObjectMother.getFlughafenBerlinToHamburgHbfJourney(), Status.SUCCESS, null),
                        new CallStatus<>(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney(), Status.SUCCESS, null)
                ));
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_executes_apiServices_in_right_order() {
        ApiTokenAndUrlInformation testData = getBlaBlaCarApiTokenAndUrl();
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Rides> body = ArgumentCaptor.forClass(Rides.class);
        ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData);

        StepVerifier.create(result)
                .thenConsumeWhile(journey -> {
                    assertThat(journey.getStatus()).isEqualTo(Status.SUCCESS);
                    assertThat(journey.getCalledObject()).isInstanceOf(Journey.class);
                    assertThat(journey.getThrowable()).isNull();
                    return true;
                })
                .verifyComplete();

        InOrder inOrder = inOrder(blaBlaCarMapperService, blaBlaCarHttpCallBuilderService, callService);
        inOrder.verify(blaBlaCarHttpCallBuilderService, times(1)).buildJourneyCoordinatesPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(callService, times(1)).get(url.capture(), httpEntity.capture());
        inOrder.verify(blaBlaCarMapperService, times(1)).buildJourneysWith(body.capture());
        assertThat(url.getValue()).isEqualTo("https://public-api.blablacar.com");
        assertThat(body.getValue()).isInstanceOf(Rides.class);
        assertThat(httpEntity.getValue()).isEqualTo(HttpEntity.EMPTY);
    }


    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_as_null_and_departure_coordinates_as_null_returns_empty_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setDepartureCoordinates(null);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_and_arrival_coordintes_as_null_returns_empty_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setArrivalCoordinates(null);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setHost(null);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        StepVerifier.create(result)
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getCalledObject()).isNull();
                    assertThat(error.getThrowable()).isInstanceOf(NullPointerException.class);
                })
                .verifyComplete();
    }

    @Test
    void test_getJourneysForRouteByCoordinates_with_apiToken_returns_failed_call_status_when_call_failed() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        when(callService.get(anyString(), any(HttpEntity.class)))
                .thenReturn(Mono.just(new ResponseEntity<>("error", HttpStatus.BAD_REQUEST)));

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());


        StepVerifier.create(result)
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getCalledObject()).isNull();
                    assertThat(error.getThrowable()).isInstanceOf(JsonParseException.class);
                })
                .verifyComplete();
    }

}
