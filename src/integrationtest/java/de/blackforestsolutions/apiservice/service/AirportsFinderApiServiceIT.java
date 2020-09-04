package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveListJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class AirportsFinderApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "airportsFinderApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation airportsFinderApiTokenAndUrlInformation;

    @Autowired
    private AirportsFinderHttpCallBuilderService airportsFinderHttpCallBuilderService;

    @Test
    void test_nearest_airport_api() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(airportsFinderApiTokenAndUrlInformation);
        testData.setDepartureCoordinates(getAirportsFinderTokenAndUrl().getDepartureCoordinates());
        testData.setPath(airportsFinderHttpCallBuilderService.buildPathWith(testData.build()));

        ResponseEntity<String> result = callService.getOld(buildUrlWith(testData.build()).toString(), airportsFinderHttpCallBuilderService.buildHttpEntityAirportsFinder(testData.build()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonToPojoFromResponse(result, AirportsFinding.class).get(0).getAirportId()).isEqualTo("17b055f6-95af-4959-935c-c08ce3f230d9");
    }

}
