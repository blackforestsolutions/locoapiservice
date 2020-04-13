package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.io.UncheckedIOException;
import java.net.URL;

class BahnRailwayStationHttpCallBuilderServiceTest {
    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnRailwayStationWith_BahnRailwayTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);

        //noinspection ConstantConditions (justification: during test runtime this will never be null)
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey()).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildBahnUrlWith_protocol_host_port_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
    }

    @Test
    void test_buildBahnUrlWith_protocol_host_port_path_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_port_path_throws_MalformedUrlException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("falseProtocol");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_protocol_host_path_correct_params_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        builder.setPort(-1);
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrongProtocol");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPathVariable_location() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();

        String result = classUnderTest.buildBahnRailwayStationPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/location/Berlin");
    }

    @Test
    void test_buildPathWith_host_pathvariable_as_null_apiVersion_locationPathVariable_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_as_null_locationPathVariable_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPathVariable_as_null_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setGermanRailLocationPath(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPathVariable_location_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setBahnLocation(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(finalTestData));
    }
}
