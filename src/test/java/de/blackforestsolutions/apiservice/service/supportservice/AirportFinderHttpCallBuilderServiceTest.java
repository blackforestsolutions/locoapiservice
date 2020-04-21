package de.blackforestsolutions.apiservice.service.supportservice;


import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.io.UncheckedIOException;
import java.net.URL;

public class AirportFinderHttpCallBuilderServiceTest {
    private final AirportsFinderHttpCallBuilderService classUnderTest = new AirportsFinderHttpCallBuilderServiceImpl();

    @Test
    public void test_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        String result = classUnderTest.buildPathWith(testData);

        org.junit.jupiter.api.Assertions.assertEquals(result, "/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }

    @Test
    public void test_buildHttpHeadersForAirportsFinderWith_AirportsFinderTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeaderForAirportsFinderWith(apiTokenAndUrlInformation);
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }


    @Test
    public void test_buildUrlWith_protocol_host_port_path_http_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        URL result = classUnderTest.buildUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getPath()).isEqualToIgnoringCase(testData.getPath());
    }

    @Test
    public void test_buildUrlWith_protocol_host_port_path_https_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
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
    public void test_buildUrlWith_wrong_protocol_host_port_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildUrlWith(finalTestData));
    }

    @Test
    public void test_buildUrlWith_protocol_host_path_http_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
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
    public void test_buildUrlWith_protocol_host_path_correct_params_https_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
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

    @Test
    public void test_buildUrlWith_wrong_protocol_host_path_throws_MalformedURLException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("wrong");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildUrlWith(finalTestData));
    }


    @Test
    public void test_buildPathWith_apiVersion_pathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    public void test_buildPathWith_everything_returns_correct_path() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder = builder.buildFrom(testData);
        testData = builder.build();
        ApiTokenAndUrlInformation finalTestData = testData;
        String result = classUnderTest.buildPathWith(finalTestData);

        Assertions.assertThat(result).isEqualTo("/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }

// todo can't check this bc value ist 0.0 which is actually valid
    /*@Test
    public void test_buildPathWith_apiVersion_pathVariable_longitude_as_null_latitude_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        departureCoordinates.setLatitude(48.1301564);
        builder.setDepartureCoordinates(departureCoordinates.build());

        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        classUnderTest.buildPathWith(finalTestData);
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }*/

    /*@Test
    public void test_buildPathWith_apiVersion_pathVariable_longitude_latidude_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        //departureCoordinates.setLatitude(null);
        departureCoordinates.setLongitude(8.2324351);
        builder.setDepartureCoordinates(departureCoordinates.build());

        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }*/


}
