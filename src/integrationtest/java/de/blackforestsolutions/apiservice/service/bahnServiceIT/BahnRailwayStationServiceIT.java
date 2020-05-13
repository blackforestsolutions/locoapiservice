package java.de.blackforestsolutions.apiservice.service.bahnServiceIT;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
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
class BahnRailwayStationServiceIT {

    @Autowired
    private CallService callService;

    @Autowired
    private BahnHttpCallBuilderService bahnHttpCallBuilderService;

    @Test
    void test_RailwayStation() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrlIT();

        ResponseEntity<String> result = callService.get(buildUrlWith(testData).toString(), bahnHttpCallBuilderService.buildHttpEntityForBahn(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}

