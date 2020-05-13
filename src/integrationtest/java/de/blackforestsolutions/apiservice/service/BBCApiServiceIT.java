package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bbc.Rides;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrlIT;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class BBCApiServiceIT {

    @Resource(name = "bbcApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bbcApiTokenAndUrlInformation;

    @Autowired
    private CallService callService;

    @Autowired
    private BBCHttpCallBuilderService bBCHttpCallBuilderService;

    @Test
    void test_journey() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDeparture(getBbcTokenAndUrlIT().getDeparture());
        testData.setArrival(getBbcTokenAndUrlIT().getArrival());
        testData.setDepartureDate(getBbcTokenAndUrl().getDepartureDate());
        testData.setPath(bBCHttpCallBuilderService.bbcBuildPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(buildUrlWith(testData.build()).toString(), bBCHttpCallBuilderService.buildHttpEntityForBbc(testData.build()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, Rides.class).getTrips()).isNotEmpty();
    }
}
