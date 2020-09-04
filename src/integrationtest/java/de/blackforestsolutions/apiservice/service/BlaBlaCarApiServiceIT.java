package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.BlaBlaCarApiService;
import de.blackforestsolutions.datamodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlaBlaCarApiServiceIT {

    @Autowired
    private BlaBlaCarApiService classUnderTest;

    @Autowired
    private ApiTokenAndUrlInformation blaBlaCarApiTokenAndUrlInformation;

    @Test
    void test_getJourneysForRouteByCoordinates_returns_journeys_with_no_failed_callStatus() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(blaBlaCarApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(52.526455d, 13.367701d).build());
        testData.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(53.553918d, 10.005147d).build());
        ZonedDateTime departureDate = LocalDate.now().plus(1, ChronoUnit.DAYS).atTime(8, 0).atZone(ZoneId.systemDefault());
        testData.setDepartureDate(departureDate);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        StepVerifier.create(result)
                .assertNext(journey -> {
                    assertThat(journey.getStatus()).isEqualTo(Status.SUCCESS);
                    assertThat(journey.getCalledObject()).isInstanceOf(Journey.class);
                    assertThat(journey.getThrowable()).isNull();
                    assertThat(journey.getCalledObject().getLegs().values().stream().findFirst().get().getStartTime().isAfter(departureDate)).isTrue();
                })
                .thenConsumeWhile(journey -> {
                    assertThat(journey.getStatus()).isEqualTo(Status.SUCCESS);
                    assertThat(journey.getCalledObject()).isInstanceOf(Journey.class);
                    assertThat(journey.getThrowable()).isNull();
                    assertThat(journey.getCalledObject().getLegs().values().stream().findFirst().get().getStartTime().isAfter(departureDate)).isTrue();
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void test_getJourneysForRouteByCoordinates_returns_empty_stream_when_no_journey_is_offered() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(blaBlaCarApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(0.0d, 0.0d).build());
        testData.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(0.0d, 0.0d).build());
        testData.setDepartureDate(LocalDate.now().plus(1, ChronoUnit.DAYS).atTime(8, 0).atZone(ZoneId.systemDefault()));

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysForRouteByCoordinates(testData.build());

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
