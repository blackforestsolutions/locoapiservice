package java.de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.searchCh.Route;
import de.blackforestsolutions.generatedcontent.searchCh.Station;
import org.apache.commons.lang.WordUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchChApiServiceIT {

    @Autowired
    private CallService callService;

    @Test
    void test_getRoute() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChRouteTokenAndUrlIT();
        ResponseEntity<String> result = callService.get(buildUrlWith(testData).toString(), buildEmptyHttpEntity());

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, Route.class).getCount()).isEqualTo(1);
    }

    @Test
    void test_SearchChStation() throws JsonProcessingException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrlIT();

        ResponseEntity<String> result = callService.get(buildUrlWith(testData).toString(), buildEmptyHttpEntity());

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, Station.class).get(0).getHtml()).contains(WordUtils.capitalize(testData.getLocationSearchTerm()));
    }
}