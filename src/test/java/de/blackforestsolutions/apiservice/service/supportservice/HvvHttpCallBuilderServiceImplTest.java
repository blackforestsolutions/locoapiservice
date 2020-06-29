package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getRosenhofHvvStation;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStadthausbrueckeHvvStation;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.convertObjectToJsonString;

class HvvHttpCallBuilderServiceImplTest {

    private final HvvHttpCallBuilderService classUnderTest = new HvvHttpCallBuilderServiceImpl();

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_start_destination_and_httpBody_returns_correct_header() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        HvvStation start = getRosenhofHvvStation();
        HvvStation destination = getStadthausbrueckeHvvStation();

        HttpHeaders result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination).getHeaders();

        Assertions.assertThat(result.getFirst(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(result.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE)).isEqualTo("HmacSHA1");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER)).isEqualTo("janhendrikhausner");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE)).isEqualTo("0fsMnAtZjmIUIk20j1F3RhI/B1Q=");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.X_TRACE_ID)).isNotNull();
        Assertions.assertThat(result.values().size()).isEqualTo(6);
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_returns_correct_http_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        String testBody = convertObjectToJsonString(getHvvJourneyBody());
        HvvStation start = getRosenhofHvvStation();
        HvvStation destination = getStadthausbrueckeHvvStation();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination);

        Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_journeyPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        String result = classUnderTest.buildJourneyPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/gti/public/getRoute");
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_as_null_journeyPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_journeyPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setJourneyPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(testData.build()));
    }

    @Test
    void test_buildStationListHttpEntityForHvv_with_apiToken_andHttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        HttpHeaders result = classUnderTest.buildStationListHttpEntityForHvv(testData).getHeaders();

        Assertions.assertThat(result.getFirst(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(result.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE)).isEqualTo("HmacSHA1");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER)).isEqualTo("janhendrikhausner");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE)).isEqualTo("u7mDccMmL3h0IXmtySBTJn0HtoA=");
        Assertions.assertThat(result.getFirst(AdditionalHttpConfiguration.X_TRACE_ID)).isNotEmpty();
        Assertions.assertThat(6).isEqualTo(result.values().size());
    }

    @Test
    void test_buildStationListHttpEntityForHvv_with_jsonBody_is_equal_to_http_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        String testBody = convertObjectToJsonString(getStationListHttpBody());

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildStationListHttpEntityForHvv(testData);

        Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildStationListPathWith_pathvariable_stationListPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        String result = classUnderTest.buildStationListPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/gti/public/listStations");
    }

    @Test
    void test_buildStationListPathWith_pathvariable_as_null_stationListPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildStationListPathWith(testData.build()));
    }

    @Test
    void test_buildStationListPathWith_pathvariable_stationListPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        builder.setStationListPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildStationListPathWith(builder.build()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_with_apiToken_andHttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        HttpHeaders result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture()).getHeaders();

        org.junit.jupiter.api.Assertions.assertEquals(result.getFirst(HttpHeaders.ACCEPT), MediaType.APPLICATION_JSON_VALUE);
        org.junit.jupiter.api.Assertions.assertEquals(result.getFirst(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON_VALUE);
        org.junit.jupiter.api.Assertions.assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE), "HmacSHA1");
        org.junit.jupiter.api.Assertions.assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER), "janhendrikhausner");
        org.junit.jupiter.api.Assertions.assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE), "OZ+Z1ogdVX5jSiltjtxMG8NQXXE=");
        org.junit.jupiter.api.Assertions.assertNotNull(result.getFirst(AdditionalHttpConfiguration.X_TRACE_ID));
        org.junit.jupiter.api.Assertions.assertEquals(6, result.values().size());
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_with_jsonBody_is_equal_to_http_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        String testBody = convertObjectToJsonString(getHvvTravelPointBody());

        HttpEntity<String> result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture());

        org.assertj.core.api.Assertions.assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_travelPointPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        String result = classUnderTest.buildTravelPointPathWith(testData);

        org.junit.jupiter.api.Assertions.assertEquals(result, "/gti/public/checkName");
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_as_null_travelPointPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(testData.build()));
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_travelPointPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setTravelPointPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(testData.build()));
    }

}
