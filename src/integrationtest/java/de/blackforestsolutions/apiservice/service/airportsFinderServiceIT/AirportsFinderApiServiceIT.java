package de.blackforestsolutions.crawler.service.airportsFinderServiceIT;

import de.blackforestsolutions.crawler.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.crawler.service.communicationservice.restcalls.AirportsFinderCallService;
import de.blackforestsolutions.crawler.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirportsFinderApiServiceIT {

    @Autowired
    AirportsFinderCallService airportsFinderCallService;

    @Autowired
    AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService;

    @Test
    public void test() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrlIT();

        ResponseEntity<String> result = airportsFinderCallService.getNearestAirports(airportsFinderHttpCallBuilderService.buildUrlWith(testData).toString(), airportsFinderHttpCallBuilderService.buildHttpEntityAirportsFinder(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

}
