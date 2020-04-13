package de.blackforestsolutions.apiservice.service.searchchIT;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.SearchChCallService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@AutoConfigureMockMvc
class SearchChRouteServiceIT {

    @Autowired
    SearchChHttpCallBuilderService searchChHttpCallBuilderService;

    @Autowired
    SearchChCallService searchChCallService;

    @Test
    void test_getRoute() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChRouteTokenAndUrlIT();
        ResponseEntity<String> result = searchChCallService.getRequestAnswer(buildUrlWith(testData).toString(), buildEmptyHttpEntity());

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
}
