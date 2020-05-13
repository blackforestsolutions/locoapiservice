package java.de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.HafasJourneyResponse;
import de.blackforestsolutions.generatedcontent.hafas.response.locations.HafasLocationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class VBBApiServiceIT {

    @Autowired
    private CallService callService;

    @Autowired
    private HafasHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = getVBBTokenAndUrl("Eiderstraße 87", null);

        ResponseEntity<String> result = callService.post(
                testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildPathWith(testData, testData.getDeparture()))),
                httpCallBuilderService.buildHttpEntityStationForHafas(testData, "Eiderstraße 87"));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, HafasLocationResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }

    @Test
    void test_getJourney() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = getVBBTokenAndUrl("770000350", "900985256");

        ResponseEntity<String> result = callService.post(
                testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildPathWith(testData, null))),
                httpCallBuilderService.buildHttpEntityJourneyForHafas(testData)
        );

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, HafasJourneyResponse.class).getSvcResL().get(0).getErr()).isEqualTo("OK");
    }

}
