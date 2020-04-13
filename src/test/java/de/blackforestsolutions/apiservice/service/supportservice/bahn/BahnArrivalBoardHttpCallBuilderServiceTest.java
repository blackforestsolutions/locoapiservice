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

class BahnArrivalBoardHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnArrivalBoardWithBahnArrivalTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);

        //noinspection ConstantConditions (justification: during test runtime this will never be null)
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey()).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_http_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getGermanRailDatePathVariable()));
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_https_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getGermanRailDatePathVariable()));
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_port_path_date_throws_MalformedUrlException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("falseProtocol");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_http_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(testData.getPath()).contains(result.getPath());
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getGermanRailDatePathVariable()));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_correct_params_https_date_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
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
        Assertions.assertThat(result.getFile()).contains(new SimpleDateFormat("yyyy-MM-dd").format(testData.getGermanRailDatePathVariable()));
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_path_date_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrongProtocol");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_stationId_datePathVariable() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        String result = classUnderTest.buildBahnArrivalBoardPathWith(testData);
        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/arrivalBoard/8011160?date=2019-07-25");
    }

    @Test
    void test_buildPathWith_pathvariable_as_null_apiVersion_arrivalBoardPathVariable_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_as_null_arrivalBoardPathVariable_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_as_null_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setGermanRailArrivalBoardPath(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_stationId_as_null_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setStationId(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(finalTestData));
    }
}
