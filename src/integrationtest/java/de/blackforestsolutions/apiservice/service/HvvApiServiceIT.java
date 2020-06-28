package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import de.blackforestsolutions.generatedcontent.hvv.response.HvvStationList;
import de.blackforestsolutions.generatedcontent.hvv.response.HvvTravelPointResponse;
import de.blackforestsolutions.generatedcontent.hvv.response.journey.HvvRoute;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Date;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class HvvApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "hvvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation hvvApiTokenAndUrlInformation;

    @Autowired
    private HvvHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationList() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hvvApiTokenAndUrlInformation);
        testData.setPath(httpCallBuilderService.buildStationListPathWith(testData.build()));

        ResponseEntity<String> result = callService.post(buildUrlWith(testData.build()).toString(), httpCallBuilderService.buildStationListHttpEntityForHvv(testData.build()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HvvStationList.class).getReturnCode()).isEqualTo("OK");
    }

    @Test
    void test_getTravelPoint() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hvvApiTokenAndUrlInformation);
        testData.setDeparture(getHvvTokenAndUrl().getDeparture());
        testData.setPath(httpCallBuilderService.buildTravelPointPathWith(testData.build()));

        ResponseEntity<String> result = callService.post(buildUrlWith(testData.build()).toString(), httpCallBuilderService.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HvvTravelPointResponse.class).getReturnCode()).isEqualTo("OK");
    }

    @Test
    void test_getJourney() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hvvApiTokenAndUrlInformation);
        testData.setDepartureDate(new Date());
        testData.setPath(httpCallBuilderService.buildJourneyPathWith(testData.build()));
        HvvStation start = TravelPointObjectMother.getRosenhofHvvStation();
        HvvStation destination = TravelPointObjectMother.getStadthausbrueckeHvvStation();

        ResponseEntity<String> result = callService.post(buildUrlWith(testData.build()).toString(), httpCallBuilderService.buildJourneyHttpEntityForHvv(testData.build(), start, destination));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, HvvRoute.class).getReturnCode()).isEqualTo("OK");
    }


}
