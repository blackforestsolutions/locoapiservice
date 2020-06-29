package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl;

class BahnRailwayStationHttpCallBuilderServiceTest {
    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnRailwayStationWith_BahnRailwayTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getBahnTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);

        //noinspection ConstantConditions (justification: during test runtime this will never be null)
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey()).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPathVariable_location() {
        ApiTokenAndUrlInformation testData = getBahnTokenAndUrl();

        String result = classUnderTest.buildBahnRailwayStationPathWith(testData, "Berlin");

        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/location/Berlin");
    }

    @Test
    void test_buildPathWith_host_pathvariable_as_null_apiVersion_locationPathVariable_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(builder.build(), "Berlin"));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_as_null_locationPathVariable_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(builder.build(), "Berlin"));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPath_as_null_location_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setLocationPath(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(builder.build(), "Berlin"));
    }

    @Test
    void test_buildPathWith_host_pathvariable_apiVersion_locationPathVariable_location_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = getBahnTokenAndUrl();

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnRailwayStationPathWith(testData, null));
    }
}
