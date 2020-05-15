package de.blackforestsolutions.apiservice.service.supportservice.searchch;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchChRouteHttpCallBuilderServiceTest {
    private final SearchChHttpCallBuilderService classUnderTest = new SearchChHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_returns_valid_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        String result = classUnderTest.buildSearchChRoutePath(testData);
        Assertions.assertThat(result).isEqualTo("/api/route.json?from=8503283&to=Zürich,+Förrlibuckstr.+60&date=04.11.2019&time=14:00&show_delays=1&num=1");
    }

    @Test
    void test_buildPathWith_pathVariable_as_null_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_as_null_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setJourneyPathVariable(null);
        testData = builder.build();


        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_as_null_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setDeparture(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_as_null_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setArrival(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_as_null_departureDate_timePathVariable_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setDatePathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_as_null_delayParameter_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setTimePathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_as_null_results_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setSearchChDelayParameter(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathVariable_searchChRoutePathVariable_departure_startLocation_arrival_destinationLocation_datePathVariable_departureDate_timePathVariable_delayParameter_results_as_null_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setSearchChResults(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildSearchChRoutePath(finalTestData));
    }
}
