package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.generatedcontent.bbc.Rides;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Date;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonPojoFromResponse;
import static org.assertj.core.api.Assertions.assertThat;

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
    void test_journey_with_string() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDeparture("Berlin");
        testData.setArrival("Hamburg");
        testData.setDepartureDate(new Date());
        testData.setPath(bBCHttpCallBuilderService.bbcBuildJourneyStringPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                HttpEntity.EMPTY
        );

        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).isNotEmpty();
        assertThat(retrieveJsonPojoFromResponse(result, Rides.class).getPager().getTotal()).isGreaterThan(0);
    }

    @Test
    void test_journeys_with_coordinates() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(52.526455d, 13.367701d).build());
        testData.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(53.553918d, 10.005147d).build());
        testData.setDepartureDate(new Date());
        testData.setDeparture("Berlin");
        testData.setArrival("Hamburg");
        testData.setPath(bBCHttpCallBuilderService.bbcBuildJourneyStringPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                HttpEntity.EMPTY
        );

        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).isNotEmpty();
        assertThat(retrieveJsonPojoFromResponse(result, Rides.class).getPager().getTotal()).isGreaterThan(0);
    }

    @Test
    void test_journeys_with_arrival_coordinates_as_null() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(52.526455d, 13.367701d).build());
        testData.setArrivalCoordinates(null);
        testData.setDepartureDate(new Date());
        testData.setDeparture("Berlin");
        testData.setArrival("Hamburg");
        testData.setPath(bBCHttpCallBuilderService.bbcBuildJourneyStringPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                HttpEntity.EMPTY
        );

        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).isNotEmpty();
        assertThat(retrieveJsonPojoFromResponse(result, Rides.class).getPager().getTotal()).isGreaterThan(0);
    }

    @Test
    void test_journey_with_arrivalDate() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(52.526455d, 13.367701d).build());
        testData.setArrivalDate(new Date());
        testData.setDeparture("Berlin");
        testData.setArrival("Hamburg");
        testData.setTimeIsDeparture(false);
        testData.setPath(bBCHttpCallBuilderService.bbcBuildJourneyStringPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                HttpEntity.EMPTY
        );

        assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).isNotEmpty();
        assertThat(retrieveJsonPojoFromResponse(result, Rides.class).getPager().getTotal()).isGreaterThan(0);
    }


}
