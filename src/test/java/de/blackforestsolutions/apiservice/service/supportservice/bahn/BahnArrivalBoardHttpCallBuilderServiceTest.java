package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl;

class BahnArrivalBoardHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnArrivalBoardWithBahnArrivalTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getBahnTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);

        //noinspection ConstantConditions (justification: during test runtime this will never be null)
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey()).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_stationId_datePathVariable() {
        ApiTokenAndUrlInformation testData = getBahnTokenAndUrl();
        String result = classUnderTest.buildBahnArrivalBoardPathWith(testData);
        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/arrivalBoard/8011160?date=2019-07-25");
    }

    @Test
    void test_buildPathWith_pathvariable_as_null_apiVersion_arrivalBoardPathVariable_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_as_null_arrivalBoardPathVariable_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_as_null_stationId_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setGermanRailArrivalBoardPath(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_arrivalBoardPathVariable_stationId_as_null_datePathVariable_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setStationId(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnArrivalBoardPathWith(builder.build()));
    }
}
