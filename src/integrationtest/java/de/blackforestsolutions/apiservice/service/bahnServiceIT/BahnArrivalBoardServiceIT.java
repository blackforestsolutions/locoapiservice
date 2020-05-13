package java.de.blackforestsolutions.apiservice.service.bahnServiceIT;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.bahn.ArrivalBoard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveListJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class BahnArrivalBoardServiceIT {

    @Autowired
    private BahnHttpCallBuilderService bahnHttpCallBuilderService;

    @Autowired
    private CallService callService;

    @Test
    void test_BahnArrivalBoard_() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrlIT();

        ResponseEntity<String> result = callService.get(buildUrlWith(testData).toString(), bahnHttpCallBuilderService.buildHttpEntityForBahn(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, ArrivalBoard.class)).isEqualTo(false);
    }
}
