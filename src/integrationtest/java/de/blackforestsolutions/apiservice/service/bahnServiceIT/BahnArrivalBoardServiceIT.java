package de.blackforestsolutions.apiservice.service.bahnServiceIT;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BahnCallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
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
class BahnArrivalBoardServiceIT {

    @Autowired
    BahnHttpCallBuilderService bahnHttpCallBuilderService;
    @Autowired
    private BahnCallService bahnCallService;

    @Test
    void test_BahnArrivalBoard_() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrlIT();

        ResponseEntity<String> result = bahnCallService.getRequestAnswer(bahnHttpCallBuilderService.buildBahnUrlWith(testData).toString(), bahnHttpCallBuilderService.buildHttpEntityForBahn(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}