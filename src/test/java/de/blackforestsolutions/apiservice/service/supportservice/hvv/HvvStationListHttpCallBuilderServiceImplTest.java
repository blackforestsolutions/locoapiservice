package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.AdditionalHttpHeadersConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class HvvStationListHttpCallBuilderServiceImplTest {

    private final HvvStationListHttpCallBuilderService classUnderTest = new HvvStationListHttpCallBuilderServiceImpl();

    @Test
    void test_buildStationListHttpEntityForHvv_with_apiToken_andHttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildStationListHttpEntityForHvv(testData);
        HttpHeaders headerResult = result.getHeaders();

        Assertions.assertThat(headerResult.getFirst(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(headerResult.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpHeadersConfiguration.GEO_FEX_AUTH_TYPE)).isEqualTo("HmacSHA1");
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpHeadersConfiguration.GEO_FEX_AUTH_USER)).isEqualTo("janhendrikhausner");
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpHeadersConfiguration.GEO_FEX_AUTH_SIGNATURE)).isEqualTo("u7mDccMmL3h0IXmtySBTJn0HtoA=");
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpHeadersConfiguration.X_TRACE_ID)).isNotEmpty();
        Assertions.assertThat(6).isEqualTo(headerResult.values().size());
    }

    @Test
    void test_buildStationListHttpEntityForHvv_with_jsonBody_is_equal_to_http_body() throws Exception {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();
        ObjectMapper mapper = new ObjectMapper();
        String testBody = mapper.writeValueAsString(HttpBodyObjectMother.getStationListHttpBodyObject());

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildStationListHttpEntityForHvv(testData);

        Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildStationListPathWith_pathvariable_stationListPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();

        String result = classUnderTest.buildStationListPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/gti/public/listStations");
    }

    @Test
    void test_buildStationListPathWith_pathvariable_as_null_stationListPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildStationListPathWith(finalTestData));
    }

    @Test
    void test_buildStationListPathWith_pathvariable_stationListPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setStationListPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildStationListPathWith(finalTestData));
    }
}
