package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BlaBlaCarHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojoFromResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class BlaBlaCarCallServiceIT {

    @Autowired
    private BlaBlaCarHttpCallBuilderService httpCallBuilderService;

    @Autowired
    private CallService callService;

    @Autowired
    private ApiTokenAndUrlInformation blaBlaCarApiTokenAndUrlInformation;

    @Test
    void test_journeys_with_coordinates() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(blaBlaCarApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(49.010518d, 8.403986d).build());
        testData.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(48.783284d, 9.182021d).build());
        testData.setDepartureDate(LocalDate.now().plus(1, ChronoUnit.DAYS).atTime(8, 0).atZone(ZoneId.systemDefault()));
        testData.setPath(httpCallBuilderService.buildJourneyCoordinatesPathWith(testData.build()));

        Mono<ResponseEntity<String>> result = callService.get(buildUrlWith(testData.build()).toString(), HttpEntity.EMPTY);

        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(responseEntity.getBody()).isNotEmpty();
                    assertThat(retrieveJsonToPojoFromResponse(responseEntity, Rides.class).getSearchInfo().getCount()).isGreaterThan(0);
                })
                .verifyComplete();
    }
}
