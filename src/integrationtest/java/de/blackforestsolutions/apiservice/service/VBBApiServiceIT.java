package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl;

@SpringBootTest
@AutoConfigureMockMvc
class VBBApiServiceIT {

    @Autowired
    private HafasCallService hafasCallService;

    @Autowired
    private HafasHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() {
        ApiTokenAndUrlInformation testData = getVBBTokenAndUrl("Eiderstraße 87", null);

        ResponseEntity<String> result = hafasCallService.getStationId(
                testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildPathWith(testData, testData.getDeparture()))),
                httpCallBuilderService.buildHttpEntityStationForHafas(testData, "Eiderstraße 87"));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

    @Test
    void test_getJourney() {
        ApiTokenAndUrlInformation testData = getVBBTokenAndUrl("770000350", "900985256");

        ResponseEntity<String> result = hafasCallService.getJourney(
                testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildPathWith(testData, null))),
                httpCallBuilderService.buildHttpEntityJourneyForHafas(testData)
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

}
