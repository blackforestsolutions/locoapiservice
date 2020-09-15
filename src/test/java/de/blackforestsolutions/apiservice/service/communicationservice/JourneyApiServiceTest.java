package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerServiceServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Status;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.util.Pair;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_2;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JourneyApiServiceTest {

    private final ExceptionHandlerService exceptionHandlerService = spy(ExceptionHandlerServiceServiceImpl.class);

    private final JourneyApiService classUnderTest = new JourneyApiServiceImpl(exceptionHandlerService);

    @Test
    void test_retrieveJourneysFromApiServices_with_userApiToken_and_set_of_apiServices_returns_journeys_as_json_asynchronously_and_sort_out_journeys() {
        List<String> expectedJsonJourneys = List.of(
                toJson(getJourneyWithEmptyFields(TEST_UUID_2)),
                toJson(getJourneyWithEmptyFields(TEST_UUID_1))
        );
        ApiTokenAndUrlInformation testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder().build();
        List<Pair<ApiTokenAndUrlInformation, Function<ApiTokenAndUrlInformation, Flux<CallStatus<Journey>>>>> testApiServices = List.of(
                Pair.of(testData, apiToken -> Flux.just(new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_2), Status.SUCCESS, null))),
                Pair.of(testData, apiToken -> Flux.just(new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.SUCCESS, null))),
                Pair.of(testData, apiToken -> Flux.just(new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.SUCCESS, null)))
        );

        Flux<String> result = classUnderTest.retrieveJourneysFromApiServices(toJson(testData), testApiServices);

        StepVerifier.create(result)
                .expectNextMatches(expectedJsonJourneys::contains)
                .expectNextMatches(expectedJsonJourneys::contains)
                .verifyComplete();
    }

    @Test
    void test_retrieveJourneysFromApiServices_returns_empty_flux_when_an_exceptions_is_thrown() {
        ArgumentCaptor<Exception> exceptionArg = ArgumentCaptor.forClass(Exception.class);

        Flux<String> result = classUnderTest.retrieveJourneysFromApiServices("", Collections.emptyList());

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();

        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getAllValues().size()).isEqualTo(1);
        assertThat(exceptionArg.getValue()).isInstanceOf(MismatchedInputException.class);
    }
}
