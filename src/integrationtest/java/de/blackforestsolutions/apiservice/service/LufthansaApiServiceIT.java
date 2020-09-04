package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@TestPropertySource(properties = {"lufthansaBearerExpirationTime=6000"})
@AutoConfigureMockMvc
class LufthansaApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    private LuftHansaHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_postAuthorizationKey() {
        AtomicReference<String> authorization = new AtomicReference<>();
        Awaitility.await()
                .atMost(Duration.ONE_SECOND)
                .untilAsserted(() -> {
                    Assertions.assertThat(lufthansaApiTokenAndUrlInformation.getAuthorization()).isNotNull();
                    authorization.set(lufthansaApiTokenAndUrlInformation.getAuthorization());
                });

        Awaitility.await()
                .atMost(Duration.TEN_SECONDS)
                .untilAsserted(() -> {
                    Assertions.assertThat(lufthansaApiTokenAndUrlInformation.getAuthorization()).isNotNull();
                    Assertions.assertThat(lufthansaApiTokenAndUrlInformation.getAuthorization()).isNotEqualTo(authorization.get());
                });
    }

    @Test
    void test_getFlights() {
        Awaitility
                .await()
                .atMost(Duration.TEN_SECONDS)
                .untilAsserted(() -> {
                    ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(lufthansaApiTokenAndUrlInformation);
                    testData.setDepartureDate(ZonedDateTime.now());
                    testData.setDeparture("ZRH");
                    testData.setArrival("FRA");
                    testData.setPath(httpCallBuilderService.buildLufthansaJourneyPathWith(testData.build()));

                    ResponseEntity<String> result = callService.getOld(buildUrlWith(testData.build()).toString(), httpCallBuilderService.buildHttpEntityForLufthansaJourney(testData.build()));

                    Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
                    Assertions.assertThat(result.getBody()).isNotEmpty();
                });
    }
}
