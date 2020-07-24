package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl;

class BritishAirwaysHttpCallBuilderServiceTest {

    private final BritishAirwaysHttpCallBuilderService classUnderTest = new BritishAirwaysHttpCallBuilderServiceImpl();

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getBritishAirwaysTokenAndUrl();

        String result = classUnderTest.buildPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/rest-v1/v1/flights;departureLocation=lhr;arrivalLocation=txl;scheduledDepartureDate=2019-10-20");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void test_britishAirways_buildHttpHeadersForBritishAirwaysWith_BritishAirwaysTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getBritishAirwaysTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBritishAirwaysWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.get(AdditionalHttpConfiguration.BA_APPLICATION).get(0)).isEqualTo(AdditionalHttpConfiguration.BA_APPLICATION_VALUE);
        Assertions.assertThat(result.get(AdditionalHttpConfiguration.BA_CLIENT_KEY).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildHttpEntityBritishAirways_with_apiToken_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = getBritishAirwaysTokenAndUrl();

        HttpEntity<String> result = classUnderTest.buildHttpEntityBritishAirways(testData);

        Assertions.assertThat(result.getHeaders()).containsKeys(AdditionalHttpConfiguration.BA_APPLICATION, AdditionalHttpConfiguration.BA_CLIENT_KEY);
        Assertions.assertThat(result.hasBody()).isFalse();
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_as_null_pathVariable_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_as_null_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        builder.setDeparture(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_as_null_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }

    @Test
    void test_britishAirways_buildPathWith_apiVersion_pathVariable_departure_arrival_departureDate_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        builder.setDepartureDate(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }
}
