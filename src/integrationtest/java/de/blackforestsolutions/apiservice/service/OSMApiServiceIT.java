package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.osm.OsmTravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveListJsonToPojoFromResponse;

@SpringBootTest
@AutoConfigureMockMvc
class OSMApiServiceIT {

    @Autowired
    private CallService callService;

    @Resource(name = "osmApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation osmApiTokenAndUrlInformation;

    @Autowired
    private OSMHttpCallBuilderService osmHttpCallBuilderService;

    @Test
    void test_getCoordinates() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(osmApiTokenAndUrlInformation);
        testData.setPath(osmHttpCallBuilderService.buildOSMPathWith(testData.build(), "Stuttgart, Waiblinger Str. 84"));

        ResponseEntity<String> result = callService.getOld(buildUrlWith(testData.build()).toString(), HttpEntity.EMPTY);

        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getBody()).isNotEmpty();
        Assertions.assertThat(retrieveListJsonToPojoFromResponse(result, OsmTravelPoint.class)).isNotEmpty();
    }
}
