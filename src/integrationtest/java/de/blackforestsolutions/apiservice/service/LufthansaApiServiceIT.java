package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;

import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import de.blackforestsolutions.generatedcontent.lufthansa.ScheduleResource;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import java.util.Date;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class LufthansaApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @Autowired
    private LuftHansaHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_postAuthorizationKey() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(lufthansaApiTokenAndUrlInformation);
        testData.setPath(httpCallBuilderService.buildLufthansaAuthorizationPathWith(testData.build()));

        ResponseEntity<String> result = callService.post(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityForLufthansaAuthorization(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, LufthansaAuthorization.class).getAccessToken()).isNotNull();
    }

    @Test
    void test_getFlights() {
        Awaitility
                .await()
                .atMost(Duration.TEN_SECONDS)
                .untilAsserted(() -> {
                    ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(lufthansaApiTokenAndUrlInformation);
                    testData.setDepartureDate(new Date());
                    testData.setDeparture("ZRH");
                    testData.setArrival("FRA");
                    testData.setPath(httpCallBuilderService.buildLufthansaJourneyPathWith(testData.build()));

                    ResponseEntity<String> result = callService.get(buildUrlWith(testData.build()).toString(), httpCallBuilderService.buildHttpEntityForLufthansaJourney(testData.build()));

                    Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
                    Assertions.assertThat(result.getBody()).isNotEmpty();
                });
    }
}
