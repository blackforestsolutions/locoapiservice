package de.blackforestsolutions.apiservice.service.supportservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

import java.io.IOException;

class VBBHttpCallBuilderServiceTest {

    private final HafasHttpCallBuilderService classUnderTest = new HafasHttpCallBuilderServiceImpl();

    @Test
    void test_buildHttpEntityStationForHafas_with_apiToken_and_station_returns_correct_body() throws IOException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("Eiderstraße 87", null);
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(HttpBodyObjectMother.getVBBLocationRequestBody());

        //noinspection rawtypes (justification: type not know before runtime)
        HttpEntity result = classUnderTest.buildHttpEntityStationForHafas(testData, "Eiderstraße 87");

        Assertions.assertEquals(expectedBody, result.getBody());
    }

    @Test
    void test_buildHttpEntityJourneyForHafas_with_apiToken_departure_and_arrival_returns_correct_body() throws IOException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("770000350", "900985256");
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(HttpBodyObjectMother.getVBBJourneyHttpBody());

        //noinspection rawtypes (justification: type not know before runtime)
        HttpEntity result = classUnderTest.buildHttpEntityJourneyForHafas(testData);

        Assertions.assertEquals(expectedBody, result.getBody());
    }

    @Test
    void test_buildPathWith_pathvariable_station_mic_mac_authorizationKey_and_requestBody_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("Eiderstraße 87", null);

        String result = classUnderTest.buildPathWith(testData, "Eiderstraße 87");

        Assertions.assertEquals("/bin/mgate.exe?mic=011e122a2d5a973bd59b733c310df01d&mac=44ddc6ff1b00b1b291be95b8d60cd8c2", result);
    }

    @Test
    void test_buildPathWith_pathvariable_station_as_null_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("770000350", "900985256");

        String result = classUnderTest.buildPathWith(testData, null);

        Assertions.assertEquals("/bin/mgate.exe?mic=397a5369bf146f66fcf73dca75b7833e&mac=48d430a92667c47058471ff027fad5bd", result);
    }
}
