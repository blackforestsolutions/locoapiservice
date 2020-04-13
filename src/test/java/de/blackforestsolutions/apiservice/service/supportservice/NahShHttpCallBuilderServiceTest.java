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

class NahShHttpCallBuilderServiceTest {

    private final HafasHttpCallBuilderService classUnderTest = new HafasHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_pathvariable_and_station_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("Eiderstraße 87", null);

        String result = classUnderTest.buildPathWith(testData, "Eiderstraße 87");

        Assertions.assertEquals("/bin/mgate.exe?", result);
    }

    @Test
    void test_buildHttpEntityStationForHafas_with_apiToken_and_departure_returns_correct_body() throws IOException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("Eiderstraße 87", null);
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(HttpBodyObjectMother.getNahShLocationRequestBody());

        //noinspection rawtypes (justification: type not know before runtime)
        HttpEntity result = classUnderTest.buildHttpEntityStationForHafas(testData, "Eiderstraße 87");

        Assertions.assertEquals(expectedBody, result.getBody());
    }

    @Test
    void test_buildPathWith_pathvariable_station_as_null_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("Eiderstraße 87", null);

        String result = classUnderTest.buildPathWith(testData, null);

        Assertions.assertEquals("/bin/mgate.exe?", result);
    }

    @Test
    void test_buildHttpEntityJourneyForHafas_with_apiToken_departure_and_arrival_returns_correct_body() throws IOException {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("981068999", "4397734");
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(HttpBodyObjectMother.getNahSHJourneyHttpBody());

        //noinspection rawtypes (justification: type not know before runtime)
        HttpEntity result = classUnderTest.buildHttpEntityJourneyForHafas(testData);

        Assertions.assertEquals(expectedBody, result.getBody());
    }

}
