package java.de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.LocationList;
import de.blackforestsolutions.generatedcontent.rmv.hafas_rest.TripList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.JAXBException;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveXmlPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class RMVApiServiceIT {

    @Autowired
    private CallService callService;

    @Autowired
    private RMVHttpCallBuilderService httpCallBuilderService;

    @Test
    void test_getStationId() throws JAXBException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("Lorch-Lorchhausen Bahnhof", "frankfurt hauptbahnhof");

        ResponseEntity<String> result = callService.get(testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildLocationPathWith(testData, testData.getArrival()))), httpCallBuilderService.buildHttpEntityStationForRMV(testData, testData.getArrival()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveXmlPojoFromResponse(result, LocationList.class).getStopLocationOrCoordLocation()).isNotEmpty();
    }

    @Test
    void test_getTrip() throws JAXBException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("003011037", "003000010");

        ResponseEntity<String> result = callService.get(testData.getProtocol().concat("://").concat(testData.getHost().concat(httpCallBuilderService.buildTripPathWith(testData))), httpCallBuilderService.buildHttpEntityTripForRMV(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveXmlPojoFromResponse(result, TripList.class).getTrip()).isNotEmpty();
    }
}
