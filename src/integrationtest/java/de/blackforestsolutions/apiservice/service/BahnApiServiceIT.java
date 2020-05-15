package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.ArrivalBoard;
import de.blackforestsolutions.generatedcontent.bahn.DepartureBoard;
import de.blackforestsolutions.generatedcontent.bahn.RailwayStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Date;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveListJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class BahnApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "bahnApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation bahnApiTokenAndUrlInformation;

    @Autowired
    private BahnHttpCallBuilderService bahnHttpCallBuilderService;

    @Test
    void test_RailwayStation() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bahnApiTokenAndUrlInformation);
        testData.setPath(bahnHttpCallBuilderService.buildBahnRailwayStationPathWith(bahnApiTokenAndUrlInformation, "Berlin Hbf"));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                bahnHttpCallBuilderService.buildHttpEntityForBahn(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, RailwayStation.class)).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, RailwayStation.class).get(0).getName()).contains("Berlin");
    }

    @Test
    void test_BahnArrivalBoard_() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bahnApiTokenAndUrlInformation);
        testData.setStationId("8011160");
        testData.setDepartureDate(new Date());
        testData.setPath(bahnHttpCallBuilderService.buildBahnArrivalBoardPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                bahnHttpCallBuilderService.buildHttpEntityForBahn(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, ArrivalBoard.class)).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, ArrivalBoard.class).get(0).getBoardId()).isEqualTo("8011160");
    }

    @Test
    void test_BahnDepartureBoard() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bahnApiTokenAndUrlInformation);
        testData.setStationId("8011160");
        testData.setDepartureDate(new Date());
        testData.setPath(bahnHttpCallBuilderService.buildBahnDepartureBoardPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                bahnHttpCallBuilderService.buildHttpEntityForBahn(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, DepartureBoard.class)).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, DepartureBoard.class).get(0).getBoardId()).isEqualTo("8011160");
    }

    @Test
    void test_BahnJourneyDetails() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bahnApiTokenAndUrlInformation);
        testData.setJourneyDetailsId("400758%2F140119%2F855402%2F294115%2F80%3fstation_evaId%3D8011160");
        testData.setPath(bahnHttpCallBuilderService.buildBahnJourneyDetailsPath(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                bahnHttpCallBuilderService.buildHttpEntityForBahn(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, DepartureBoard.class)).isEqualTo(false);
    }

}
