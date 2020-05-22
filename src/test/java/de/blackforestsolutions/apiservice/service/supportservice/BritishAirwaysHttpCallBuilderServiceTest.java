package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

class BritishAirwaysHttpCallBuilderServiceTest {

    private final BritishAirwaysHttpCallBuilderService classUnderTest = new BritishAirwaysHttpCallBuilderServiceImpl();

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();

        String result = classUnderTest.buildPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/rest-v1/v1/flights;departureLocation=lhr;arrivalLocation=txl;scheduledDepartureDate=2019-10-20");
    }

    @Test
    void test_britishAirways_buildHttpHeadersForBritishAirwaysWith_BritishAirwaysTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBritishAirwaysWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.get(AdditionalHttpConfiguration.BA_APPLICATION).get(0)).isEqualTo(AdditionalHttpConfiguration.BA_APPLICATION_VALUE);
        Assertions.assertThat(result.get(AdditionalHttpConfiguration.BA_CLIENT_KEY).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildHttpEntityBritishAirways_with_apiToken_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();

        HttpEntity<String> result = classUnderTest.buildHttpEntityBritishAirways(testData);

        Assertions.assertThat(result.getHeaders()).containsKeys(AdditionalHttpConfiguration.BA_APPLICATION, AdditionalHttpConfiguration.BA_CLIENT_KEY);
        Assertions.assertThat(result.hasBody()).isFalse();
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_as_null_pathVariable_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();
        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_as_null_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setDeparture(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_as_null_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_departureDate_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setDepartureDate(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }
}
