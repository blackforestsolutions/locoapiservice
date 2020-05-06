package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.OSMCallService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@AutoConfigureMockMvc
class OSMApiServiceIT {

    @Autowired
    OSMCallService osmCallService;

    @Autowired
    OSMHttpCallBuilderService osmHttpCallBuilderService;

    @Test
    void test_getCoordinates() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrlIT();

        ResponseEntity<String> result = osmCallService.getTravelPoints(buildUrlWith(testData).toString(), buildEmptyHttpEntity());

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
