package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.net.URL;

class BbcHttpCallBuilderServiceTest {

    private final BBCHttpCallBuilderService classUnderTest = new BBCHttpCallBuilderServiceImpl();

    @Test
    void test_bbc_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        String result = classUnderTest.bbcBuildPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/api/v2/trips?fn=ZRH&tn=FRA&db=2019-12-28");
    }

    @Test
    void test_buildHttpHeadersForBbcWith_BbcTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBbcWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
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
    void test_buildUrlWith_protocol_host_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
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
    void test_buildUrlWith_protocol_host_path_correct_params_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
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
}
