package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpCallBuilderServiceTest {

    @Test
    void test_buildUrlWith_protocol_host_port_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();

        URL result = buildUrlWith(testData);

        assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        assertThat(result.getPort()).isEqualTo(testData.getPort());
        assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();

        URL result = buildUrlWith(testData);

        assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        assertThat(result.getPort()).isEqualTo(testData.getPort());
        assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_port_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> buildUrlWith(finalTestData));
    }

    @Test
    void test_buildUrlWith_protocol_host_path_http_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();

        URL result = buildUrlWith(testData);

        assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        assertThat(result.getPort()).isEqualTo(testData.getPort());
        assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_protocol_host_path_correct_params_https_returns_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        builder.setPort(-1);
        testData = builder.build();

        URL result = buildUrlWith(testData);

        assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        assertThat(result.getPort()).isEqualTo(testData.getPort());
        assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    void test_buildUrlWith_wrong_protocol_host_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> buildUrlWith(finalTestData));
    }

    @Test
    void test_setFormatToJsonFor_with_basic_Httpheader_returns_json_Httpheader() {
        HttpHeaders testData = new HttpHeaders();
        setFormatToJsonFor(testData);

        assertThat(Objects.requireNonNull(testData.get(HttpHeaders.ACCEPT)).get(0)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void test_setFormatToXmlFor_with_basic_Httpheader_returns_xml_Httpheader() {
        HttpHeaders testData = new HttpHeaders();
        setFormatToXmlFor(testData);

        assertThat(Objects.requireNonNull(testData.get(HttpHeaders.ACCEPT)).get(0)).isEqualTo(MediaType.APPLICATION_XML_VALUE);
    }
}
