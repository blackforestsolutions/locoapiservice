package de.blackforestsolutions.apiservice.service.supportservice.searchch;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class SearchChStationHttpCallBuilderServiceTest {

    private final SearchChHttpCallBuilderService classUnderTest = new SearchChHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_returns_valid_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        String result = classUnderTest.buildSearchChLocationPath(testData);
        Assertions.assertThat(result).isEqualTo("/api/completion.json?term=lu&show_ids=1&show_coordinates=1");
    }

    @Test
    void test_buildPathWith_pathVariable_as_null_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_as_null_termParameter_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setLocationPath(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_as_null_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setSearchChTermParameter(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_as_null_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setLocationSearchTerm(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_as_null_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setSearchChStationId(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_as_null_throws_NullpointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setSearchChStationCoordinateParameter(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(finalTestData)
        );
    }
}
