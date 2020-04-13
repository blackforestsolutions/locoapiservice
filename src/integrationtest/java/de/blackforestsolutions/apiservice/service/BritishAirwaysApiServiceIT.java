package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BritishAirwaysCallService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BritishAirwaysApiServiceIT {

    @Autowired
    BritishAirwaysCallService britishAirwaysApiService;

    @Autowired
    BritishAirwaysHttpCallBuilderService httpCallBuilderService;

    @Test
    void test() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrlIT();

        ResponseEntity<String> result = britishAirwaysApiService.getFlights(httpCallBuilderService.buildUrlWith(testData).toString(), httpCallBuilderService.buildHttpEntityBritishAirways(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

}
