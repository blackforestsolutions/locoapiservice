package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LuftHansaHttpCallBuilderServiceTest {

    private final LuftHansaHttpCallBuilderService classUnderTest = new LuftHansaHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();

        String result = classUnderTest.buildLuftHansaPathWith(testData);

        Assertions.assertEquals(result, "/v1/operations/schedules/ZRH/FRA/2019-06-28");
    }

    @Test
    void test_buildPathWith_apiVersion_as_null_pathVariable_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLuftHansaPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_apiVersion_pathvariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLuftHansaPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_as_null_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setDeparture(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLuftHansaPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_arrival_as_null_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setArrival(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLuftHansaPathWith(finalTestData));
    }
}
