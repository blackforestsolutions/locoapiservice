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
import java.time.ZonedDateTime;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class VBBApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "vbbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation vbbApiTokenAndUrlInformation;

    @Autowired
    private HafasHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(vbbApiTokenAndUrlInformation);
        testData.setPath(httpCallBuilderService.buildPathWith(testData.build(), "Eiderstraße 87"));

        ResponseEntity<String> result = callService.post(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityStationForHafas(testData.build(), "Eiderstraße 87")
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HafasLocationResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }

    @Test
    void test_getJourney() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(vbbApiTokenAndUrlInformation);
        testData.setDeparture("770000350");
        testData.setDepartureDate(ZonedDateTime.now());
        testData.setArrival("900985256");
        testData.setPath(httpCallBuilderService.buildPathWith(testData.build(), null));

        ResponseEntity<String> result = callService.post(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityJourneyForHafas(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HafasJourneyResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }

}
