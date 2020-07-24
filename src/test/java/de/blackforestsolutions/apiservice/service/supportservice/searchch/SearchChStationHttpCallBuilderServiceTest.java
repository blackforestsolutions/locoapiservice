package de.blackforestsolutions.apiservice.service.supportservice.searchch;

import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl;


class SearchChStationHttpCallBuilderServiceTest {

    private final SearchChHttpCallBuilderService classUnderTest = new SearchChHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_returns_valid_string() {
        ApiTokenAndUrlInformation testData = getSearchChTokenAndUrl();
        String result = classUnderTest.buildSearchChLocationPath(testData, "lu");
        Assertions.assertThat(result).isEqualTo("/api/completion.json?term=lu&show_ids=1&show_coordinates=1");
    }

    @Test
    void test_buildPathWith_pathVariable_as_null_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(builder.build(), "lu")
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_as_null_termParameter_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        builder.setLocationPath(null);

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(builder.build(), "lu")
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_as_null_searchTerm_stationIdParameter_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        builder.setSearchChTermParameter(null);

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(builder.build(), "lu")
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_as_null_coordinateParameter_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        builder.setSearchChStationId(null);

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(builder.build(), "lu")
        );
    }

    @Test
    void test_buildPathWith_pathVariable_locationPath_termParameter_searchTerm_stationIdParameter_coordinateParameter_as_null_throws_NullpointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        builder.setSearchChStationCoordinateParameter(null);

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> classUnderTest.buildSearchChLocationPath(builder.build(), "lu")
        );
    }
}
