package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpHeadersConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.io.UncheckedIOException;
import java.net.URL;

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

        Assertions.assertThat(result.get(AdditionalHttpHeadersConfiguration.BA_APPLICATION)).contains(AdditionalHttpHeadersConfiguration.BA_APPLICATION_VALUE);
        Assertions.assertThat(result.get(AdditionalHttpHeadersConfiguration.BA_CLIENT_KEY)).contains(apiTokenAndUrlInformation.getAuthorization());
    }


    @Test
    void test_britishAirways_buildUrlWith_protocol_host_port_path_http_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_britishAirways_buildUrlWith_protocol_host_port_path_https_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_britishAirways_buildUrlWith_wrong_protocol_host_port_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildUrlWith(finalTestData));
    }

    @Test
    void test_britishAirways_buildUrlWith_protocol_host_path_http_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_britishAirways_buildUrlWith_protocol_host_path_correct_params_https_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        builder.setPort(-1);
        testData = builder.build();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_britishAirways_buildUrlWith_wrong_protocol_host_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl();
        ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildUrlWith(finalTestData));
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
