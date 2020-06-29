package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.britishairways.FlightsResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class BritishAirwaysApiServiceIT {

    @Resource(name = "britishAirwaysApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation britishAirwaysApiTokenAndUrlInformation;

    @Autowired
    private CallService callService;

    @Autowired
    private BritishAirwaysHttpCallBuilderService httpCallBuilderService;

    @Test
    void test() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(britishAirwaysApiTokenAndUrlInformation);
        testData.setDeparture(getBritishAirwaysTokenAndUrl().getDeparture());
        testData.setArrival(getBritishAirwaysTokenAndUrl().getArrival());
        testData.setDepartureDate(ZonedDateTime.now());
        testData.setPath(httpCallBuilderService.buildPathWith(testData.build()));

        ResponseEntity<String> result = callService.get(
                buildUrlWith(testData.build()).toString(),
                httpCallBuilderService.buildHttpEntityBritishAirways(testData.build())
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonToPojoFromResponse(result, FlightsResponse.class).getAdditionalProperties()).isNotEmpty();
    }

}
