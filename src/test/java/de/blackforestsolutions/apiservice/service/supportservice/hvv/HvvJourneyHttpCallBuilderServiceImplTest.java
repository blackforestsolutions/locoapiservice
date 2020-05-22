package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


class HvvJourneyHttpCallBuilderServiceImplTest {

    private final HvvJourneyHttpCallBuilderService classUnderTest = new HvvJourneyHttpCallBuilderServiceImpl();

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_start_destination_and_HttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        HvvStation start = TravelPointObjectMother.getRosenhofHvvStation();
        HvvStation destination = TravelPointObjectMother.getStadthausbrueckeHvvStation();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination);
        HttpHeaders headerResult = result.getHeaders();

        Assertions.assertThat(headerResult.getFirst(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(headerResult.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE)).isEqualTo("HmacSHA1");
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER)).isEqualTo("janhendrikhausner");
        Assertions.assertThat(headerResult.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE)).isEqualTo("n1fA4PW5/7DtcnPueRXI9m7lDKw=");

        Assertions.assertThat(headerResult.getFirst(AdditionalHttpConfiguration.X_TRACE_ID)).isNotNull();
        Assertions.assertThat(headerResult.values().size()).isEqualTo(6);
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_returns_correct_http_body() throws Exception {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        ObjectMapper mapper = new ObjectMapper();
        String testBody = mapper.writeValueAsString(HttpBodyObjectMother.getHvvJourneyBodyObject());
        HvvStation start = TravelPointObjectMother.getRosenhofHvvStation();
        HvvStation destination = TravelPointObjectMother.getStadthausbrueckeHvvStation();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination);

        Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }


    @Test
    void test_buildJourneyPathWith_pathvariable_journeyPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();

        String result = classUnderTest.buildJourneyPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/gti/public/getRoute");
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_as_null_journeyPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(finalTestData));
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_journeyPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setJourneyPathVariable(null);
        testData = builder.build();

        final ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(finalTestData));
    }
}
