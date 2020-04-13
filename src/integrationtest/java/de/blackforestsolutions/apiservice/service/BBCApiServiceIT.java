package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BBCCallService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
class BBCApiServiceIT {

    @Autowired
    BBCCallService bbcApiService;

    @Autowired
    BBCHttpCallBuilderService bBCHttpCallBuilderService;

    @Test
    void test_() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrlIT();

        String httpVariabel = "?fn=ZRH&tn=FRA&db=2019-10-20";
        ResponseEntity<String> result = bbcApiService.getRide(bBCHttpCallBuilderService.buildUrlWith(testData).toString().concat(httpVariabel), bBCHttpCallBuilderService.buildHttpEntityForBbc(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}
