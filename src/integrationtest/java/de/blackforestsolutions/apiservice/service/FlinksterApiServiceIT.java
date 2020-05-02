package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.FlinksterCallService;
import de.blackforestsolutions.apiservice.service.supportservice.FlinksterHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@AutoConfigureMockMvc
public class FlinksterApiServiceIT {

    @Autowired
    private FlinksterCallService flinksterCallService;

    @Autowired
    private FlinksterHttpCallBuilderService flinksterHttpCallBuilderService;

    @Test
    void test_getRides() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getFlinksterJourneyDetailsTokenAndUrl();

        ResponseEntity<String> result = flinksterCallService.getRides(buildUrlWith(testData).toString(), flinksterHttpCallBuilderService.buildHttpEntityForFlinkster(testData));

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
