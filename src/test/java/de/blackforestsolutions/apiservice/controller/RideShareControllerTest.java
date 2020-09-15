package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.service.communicationservice.BlaBlaCarApiService;
import de.blackforestsolutions.apiservice.service.communicationservice.JourneyApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBlaBlaCarApiTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getUserRequestTokenSerialized;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.toJson;
import static org.apache.commons.lang.StringUtils.deleteWhitespace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;

class RideShareControllerTest {

    private final JourneyApiService journeyApiService = spy(JourneyApiService.class);
    private final BlaBlaCarApiService blaBlaCarApiService = spy(BlaBlaCarApiService.class);

    private final WebTestClient classUnderTest = bindToController(new RideShareController(journeyApiService, blaBlaCarApiService, getBlaBlaCarApiTokenAndUrl())).build();

    @Captor
    private ArgumentCaptor<List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>>> apiServices;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_if_call_is_executed_correctly_and_return_journeys() {
        ArgumentCaptor<String> requestToken = ArgumentCaptor.forClass(String.class);
        when(journeyApiService.retrieveJourneysFromApiServices(anyString(), anyList()))
                .thenReturn(Flux.just(toJson(getJourneyWithEmptyFields(TEST_UUID_1))));

        Flux<String> result = classUnderTest
                .post()
                .uri("/ride-shares/get")
                .body(Mono.just(getUserRequestTokenSerialized()), String.class)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(deleteWhitespace(journey)).isEqualTo(deleteWhitespace(toJson(getJourneyWithEmptyFields(TEST_UUID_1)))))
                .verifyComplete();
        verify(journeyApiService, times(1)).retrieveJourneysFromApiServices(requestToken.capture(), apiServices.capture());
        assertThat(requestToken.getValue()).isEqualTo(getUserRequestTokenSerialized());
        assertThat(apiServices.getValue().size()).isEqualTo(1);
    }

    @Test
    void test_if_call_is_executed_correctly_when_no_results_are_available() {
        when(journeyApiService.retrieveJourneysFromApiServices(anyString(), anyList()))
                .thenReturn(Flux.empty());

        Flux<String> result = classUnderTest
                .post()
                .uri("/ride-shares/get")
                .body(Mono.just(getUserRequestTokenSerialized()), String.class)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}
