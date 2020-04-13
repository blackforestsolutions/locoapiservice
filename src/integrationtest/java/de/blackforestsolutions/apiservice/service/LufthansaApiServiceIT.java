package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.LufthansaCallService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@AutoConfigureMockMvc
class LufthansaApiServiceIT {

    @Autowired
    LufthansaCallService lufthansaApiService;

    @Autowired
    LuftHansaHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getFlights() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrlIT();

        ResponseEntity<String> result = lufthansaApiService.getFlights(buildUrlWith(testData).toString(), httpCallBuilderService.buildHttpEntityForLuftHansa(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}
