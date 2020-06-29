package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl;

class BahnDepartureBoardHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnDepartureBoardWithBahnArrivalTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getBahnTokenAndUrl();
        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureBoardPathVariable_stationId_departureDate() {
        ApiTokenAndUrlInformation testData = getBahnTokenAndUrl();
        String result = classUnderTest.buildBahnDepartureBoardPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/departureBoard/8011160?date=2019-07-25");
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_as_null_apiVersion_departureBoardPathVariable_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(builder.build()));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_as_null_departureBoardPathVariable_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(builder.build()));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_departureBoardPathVariable_as_null_stationId_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setGermanRailDepartureBoardPath(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(builder.build()));
    }

    @Test
    void test_buildBahnDepartureBoardPathWith_pathvariable_apiVersion_departureBoardPathVariable_stationId_as_null_departureDate_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setStationId(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnDepartureBoardPathWith(builder.build()));
    }

}
