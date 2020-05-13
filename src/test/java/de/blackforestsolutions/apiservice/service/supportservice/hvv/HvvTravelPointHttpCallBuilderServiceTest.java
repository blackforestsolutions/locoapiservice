package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class HvvTravelPointHttpCallBuilderServiceTest {

    private final HvvTravelPointHttpCallBuilderService classUnderTest = new HvvTravelPointHttpCallBuilderServiceImpl();

    @Test
    void test_buildTravelPointHttpEntityForHvv_with_apiToken_andHttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture());
        HttpHeaders headerResult = result.getHeaders();

        Assertions.assertEquals(headerResult.getFirst(HttpHeaders.ACCEPT), MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertEquals(headerResult.getFirst(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertEquals(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE), "HmacSHA1");
        Assertions.assertEquals(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER), "janhendrikhausner");
        Assertions.assertEquals(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE), "YCixqJ8OE8mXlQeeDy1njayI5O0=");

        Assertions.assertNotNull(headerResult.getFirst(AdditionalHttpConfiguration.X_TRACE_ID));
        Assertions.assertEquals(6, headerResult.values().size());
    }

    @Test
    void test_buildStationListHttpEntityForHvv_with_jsonBody_is_equal_to_http_body() throws Exception {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();
        ObjectMapper mapper = new ObjectMapper();
        String testBody = mapper.writeValueAsString(HttpBodyObjectMother.getHvvTravelPointBodyObject());

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture());

        org.assertj.core.api.Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_travelPointPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();

        String result = classUnderTest.buildTravelPointPathWith(testData);

        Assertions.assertEquals(result, "/gti/public/checkName");
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_as_null_travelPointPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(finalTestData));
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_travelPointPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setTravelPointPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(finalTestData));
    }
}
