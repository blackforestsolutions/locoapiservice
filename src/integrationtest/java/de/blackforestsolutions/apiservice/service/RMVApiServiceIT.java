package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.RMVCallService;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl;

@SpringBootTest
@AutoConfigureMockMvc
class RMVApiServiceIT {

    @Autowired
    RMVCallService rmvCallService;

    @Autowired
    RMVHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl();

        ResponseEntity<String> result = rmvCallService.getStationId(testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildLocationPathWith(testData, testData.getArrival()))), httpCallBuilderService.buildHttpEntityStationForRMV(testData, testData.getArrival()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

    @Test
    void test_getTrip() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl();

        ResponseEntity<String> result = rmvCallService.getTrip(testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildTripPathWith(testData))), httpCallBuilderService.buildHttpEntityTripForRMV(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}
