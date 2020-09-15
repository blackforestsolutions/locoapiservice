package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.datamodel.exception.CompromisedAttributeException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.CallStatusListObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getHvvHauptbahnhofTravelPoint;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerServiceTest {

    private final ExceptionHandlerService classUnderTest = new ExceptionHandlerServiceServiceImpl();

    @Test
    void test_handleExceptionsWith_CallStatuses() throws CompromisedAttributeException {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = retrieveCallStatusList();

        Map<UUID, Journey> result = classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(getBerlinHbfToHamburgLandwehrJourney().getId()).getId()).isEqualTo(getBerlinHbfToHamburgLandwehrJourney().getId());
        assertThat(result.get(getBerlinHbfToHamburgLandwehrJourney().getId()).getArrivalTimeFromJourney()).isEqualTo(getBerlinHbfToHamburgLandwehrJourney().getArrivalTimeFromJourney());
        assertThat(result.get(getBerlinHbfToHamburgLandwehrJourney().getId()).getLegs().entrySet().size()).isEqualTo(1);
    }

    @Test
    void test_handleExceptionsTravelPoints_with_travelPoint() {
        TravelPoint travelPoint = getHvvHauptbahnhofTravelPoint();
        TravelPointStatus travelPointStatus = new TravelPointStatus(Optional.of(travelPoint), Optional.empty());
        LinkedHashSet<TravelPointStatus> travelPointStatuses = new LinkedHashSet<>();
        travelPointStatuses.add(travelPointStatus);
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(travelPointStatuses, Status.SUCCESS, null);

        LinkedHashSet<TravelPoint> result = classUnderTest.handleExceptionsTravelPoints(testData);

        //noinspection OptionalGetWithoutIsPresent (justification: it`s a test and we know that the get is not null)
        assertThat(result.stream().findAny().get()).isEqualToComparingFieldByField(travelPoint);
    }

    @Test
    void test_handleExceptions_with_TravelPoint() {
        TravelPoint travelPoint = new TravelPoint.TravelPointBuilder().setCity("Test").build();
        TravelPointStatus travelPointStatus = new TravelPointStatus(Optional.of(travelPoint), Optional.empty());
        CallStatus<TravelPointStatus> testData = new CallStatus<>(travelPointStatus, Status.SUCCESS, null);

        TravelPoint result = classUnderTest.handleExceptionsOld(testData);

        assertThat(result).isEqualToComparingFieldByField(travelPoint);
    }

    @Test
    void test_handleExceptionsTravelPoints_with_travelPoint_exception() {
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(null, Status.FAILED, new Exception("Test"));

        LinkedHashSet<TravelPoint> result = classUnderTest.handleExceptionsTravelPoints(testData);

        assertThat(result.size()).isEqualTo(0);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void test_handleExceptions_with_TravelPoint_exception() {
        CallStatus<TravelPointStatus> testData = new CallStatus<>(null, Status.FAILED, new Exception("Test"));

        TravelPoint result = classUnderTest.handleExceptionsOld(testData);

        assertThat(result).isEqualToComparingFieldByField(new TravelPoint.TravelPointBuilder().build());
    }

    @Test
    void test_handleExceptionsWith_exception_in_root_delivers_empty_list() {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = retrieveCallStatusListFailed();

        Map<UUID, Journey> result = classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptionsTravelPoints_with_exception_in_root_delivers_empty_list() {
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(null, Status.SUCCESS, new Exception("Test"));

        LinkedHashSet<TravelPoint> result = classUnderTest.handleExceptionsTravelPoints(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptions_with_exception_in_root_and_successful_callStatus_delivers_empty_travelPoint() {
        CallStatus<TravelPointStatus> testData = new CallStatus<>(null, Status.SUCCESS, new Exception("Test"));

        TravelPoint result = classUnderTest.handleExceptionsOld(testData);

        assertThat(result).isEqualTo(new TravelPoint.TravelPointBuilder().build());
    }

    @Test
    void test_handleExceptions_with_exception_in_root_and_successful_callStatus_delivers_empty_journeyMap() {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = retrieveCallStatusListSuccessWithEmptyMapAndException();

        Map<UUID, Journey> result = classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptions_with_allParameters_as_null_returns_empty_travelPoint() {
        //noinspection ConstantConditions { service should throw no exception immediately when all parameters are null }
        CallStatus<TravelPointStatus> testData = new CallStatus<>(null, null, null);

        TravelPoint result = classUnderTest.handleExceptionsOld(testData);

        assertThat(result).isEqualTo(new TravelPoint.TravelPointBuilder().build());
    }

    @Test
    void test_handleExceptionsTravelPoints_with_allParameters_as_null_returns_empty_hashset() {
        //noinspection ConstantConditions { service should throw no exception immediately when all parameters are null }
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(null, null, null);

        LinkedHashSet<TravelPoint> result = classUnderTest.handleExceptionsTravelPoints(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptions_with_allParameters_as_null_returns_empty_journeyMap() {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = retrieveCallStatusListWithAllParametersAsNull();

        Map<UUID, Journey> result = classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptions_with_TravelPointStatus_exception() {
        Problem problem = new Problem.ProblemBuilder().setExceptions(List.of(new Exception("Test"))).build();
        CallStatus<TravelPointStatus> testData = new CallStatus<>(
                new TravelPointStatus(Optional.empty(), Optional.of(problem)),
                Status.SUCCESS,
                null
        );

        TravelPoint result = classUnderTest.handleExceptionsOld(testData);

        assertThat(result).isEqualToComparingFieldByField(new TravelPoint.TravelPointBuilder().build());
    }

    @Test
    void test_handleExceptionsTravelPoints_with_linkedHashSet_of_travelPointStatus_exception_returns_empty_linkedHashset() {
        Problem problem = new Problem.ProblemBuilder().setExceptions(List.of(new Exception("Test"))).build();
        TravelPointStatus travelPointStatus = new TravelPointStatus(Optional.empty(), Optional.of(problem));
        LinkedHashSet<TravelPointStatus> travelPointStatuses = new LinkedHashSet<>();
        travelPointStatuses.add(travelPointStatus);
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(travelPointStatuses, Status.SUCCESS, null);

        LinkedHashSet<TravelPoint> result = classUnderTest.handleExceptionsTravelPoints(testData);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void test_handleExceptions_with_list_of_journeyStatusProblems_returns_empty_journeyMap() {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = retrieveCallStatusListWithJourneyStatusProblem();

        Map<UUID, Journey> result = classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(0);
    }


    /*
        Reactive ExceptionHandlerService
     */
    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_null_exception_as_null_returns_emptyMono() {
        //noinspection ConstantConditions because it could be still null
        CallStatus<Journey> testData = new CallStatus<>(null, null, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_null_exception_as_null_returns_emptyMono() {
        //noinspection ConstantConditions because it could be still null
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), null, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_null_exception_returns_emptyMono() {
        //noinspection ConstantConditions because it could be still null
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), null, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_null_exception_returns_emptyMono() {
        //noinspection ConstantConditions because it could be still null
        CallStatus<Journey> testData = new CallStatus<>(null, null, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_success_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.SUCCESS, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_success_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.SUCCESS, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNext(getJourneyWithEmptyFields(TEST_UUID_1))
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_success_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.SUCCESS, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_success_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.SUCCESS, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_failed_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.FAILED, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_failed_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.FAILED, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_failed_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(TEST_UUID_1), Status.FAILED, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_failed_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.FAILED, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}
