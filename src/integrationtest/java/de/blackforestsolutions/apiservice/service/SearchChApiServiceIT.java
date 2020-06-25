package de.blackforestsolutions.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
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

import javax.annotation.Resource;
import java.util.Date;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildEmptyHttpEntity;
import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonPojoFromResponse;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveListJsonPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class SearchChApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "searchApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation searchApiTokenAndUrlInformation;

    @Autowired
    private SearchChHttpCallBuilderService searchChHttpCallBuilderService;

    @Test
    void test_getRoute() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(searchApiTokenAndUrlInformation);
        testData.setDeparture("8503283");
        testData.setArrival("Zürich,+Förrlibuckstr.+60");
        testData.setDepartureDate(new Date());
        testData.setPath(searchChHttpCallBuilderService.buildSearchChRoutePath(testData.build()));

        ResponseEntity<String> result = callService.get(buildUrlWith(testData.build()).toString(), buildEmptyHttpEntity());

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveJsonPojoFromResponse(result, Route.class).getCount()).isEqualTo(1);
    }

    @Test
    void test_SearchChStation() throws JsonProcessingException {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(searchApiTokenAndUrlInformation);
        testData.setPath(searchChHttpCallBuilderService.buildSearchChLocationPath(testData.build(), "Einsiedeln"));

        ResponseEntity<String> result = callService.get(buildUrlWith(testData.build()).toString(), buildEmptyHttpEntity());

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonPojoFromResponse(result, Station.class).get(0).getHtml()).contains(WordUtils.capitalize("Einsiedeln"));
    }
}