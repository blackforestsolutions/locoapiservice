package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.HafasJourneyResponse;
import de.blackforestsolutions.generatedcontent.hafas.response.locations.HafasLocationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getDBTokenAndUrl;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class DBApiServiceIT {

    @Resource(name = "dbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation dbApiTokenAndUrlInformation;

    @Autowired
    private CallService hafasCallService;

    @Autowired
    private HafasHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(dbApiTokenAndUrlInformation);
        testData.setPath(httpCallBuilderService.buildPathWith(testData.build(), "Eiderstraße 87"));

        ResponseEntity<String> result = hafasCallService.post(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityStationForHafas(testData.build(), "Eiderstraße 87")
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HafasLocationResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }

    @Test
    void test_getJourney() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(dbApiTokenAndUrlInformation);
        testData.setDeparture("981067408");
        testData.setArrival("000362734");
        testData.setDepartureDate(getDBTokenAndUrl(null, null).getDepartureDate());
        testData.setPath(httpCallBuilderService.buildPathWith(testData.build(), null));

        ResponseEntity<String> result = hafasCallService.post(
                testData.getProtocol()
                        .concat("://")
                        .concat(testData.getHost())
                        .concat(httpCallBuilderService.buildPathWith(testData.build(), null)),
                httpCallBuilderService.buildHttpEntityJourneyForHafas(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HafasJourneyResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }
}
