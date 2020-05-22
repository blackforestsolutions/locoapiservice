package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.LocationList;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.TripList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveXmlPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class RMVApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "rmvApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation rmvApiTokenAndUrlInformation;

    @Autowired
    private RMVHttpCallBuilderService httpCallBuilderService;


    @Test
    void test_getStationId() throws JAXBException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(rmvApiTokenAndUrlInformation);
        testData.setPath(httpCallBuilderService.buildLocationPathWith(testData.build(), "frankfurt hauptbahnhof"));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityStationForRMV(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveXmlPojoFromResponse(result, LocationList.class).getStopLocationOrCoordLocation()).isNotEmpty();
    }

    @Test
    void test_getTrip() throws JAXBException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(rmvApiTokenAndUrlInformation);
        testData.setDeparture("003011037");
        testData.setArrival("003000010");
        testData.setPath(httpCallBuilderService.buildTripPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityTripForRMV(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveXmlPojoFromResponse(result, TripList.class).getTrip()).isNotEmpty();
    }
}
