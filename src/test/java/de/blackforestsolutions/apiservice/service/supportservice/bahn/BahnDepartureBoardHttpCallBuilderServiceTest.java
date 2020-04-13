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
import java.text.SimpleDateFormat;

class BahnDepartureBoardHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnDepartureBoardWithBahnArrivalTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_http_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getDepartureDate()));
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_https_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getDepartureDate()));
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_port_path_date_throws_MalformedUrlException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("falseProtocol");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_http_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getDepartureDate()));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_correct_params_https_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        builder.setPort(-1);
        testData = builder.build();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getDepartureDate()));
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_path_date_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrongProtocol");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureBoardPathVariable_stationId_departureDate() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        String result = classUnderTest.buildBahnDepartureBoardPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/departureBoard/8011160?date=2019-07-25");
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_as_null_apiVersion_departureBoardPathVariable_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(finalTestData));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_as_null_departureBoardPathVariable_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(finalTestData));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_departureBoardPathVariable_as_null_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setGermanRailDepartureBoardPath(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(finalTestData));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_departureBoardPathVariable_stationId_as_null_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setStationId(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(finalTestData));
    }

}
