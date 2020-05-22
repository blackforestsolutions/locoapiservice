package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
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
    void test_journey() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(bbcApiTokenAndUrlInformation);
        testData.setDeparture("Berlin");
        testData.setArrival("Hamburg");
        testData.setDepartureDate(new Date());
        testData.setPath(bBCHttpCallBuilderService.bbcBuildPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                bBCHttpCallBuilderService.buildHttpEntityForBbc(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}
