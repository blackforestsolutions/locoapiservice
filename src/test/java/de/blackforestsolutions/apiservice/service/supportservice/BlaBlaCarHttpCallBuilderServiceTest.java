package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBlaBlaCarApiTokenAndUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlaBlaCarHttpCallBuilderServiceTest {

    private final BlaBlaCarHttpCallBuilderService classUnderTest = new BlaBlaCarHttpCallBuilderServiceImpl();

    @Test
    void test_buildJourneyCoordinatesPathWith_departureCoordinates_arrivalCoordinates_departureDate_and_arrivalDate_returns_valid_path() {
        ApiTokenAndUrlInformation testData = getBlaBlaCarApiTokenAndUrl();

        String result = classUnderTest.buildJourneyCoordinatesPathWith(testData);

        assertThat(result).isEqualTo("/api/v3/trips?key=7f529ec36ab542b78e63f5270a621837&from_coordinate=52.526455,13.367701&to_coordinate=53.553918,10.005147&from_country=DE&to_country=DE&locale=de_DE&currency=EUR&start_date_local=2020-09-01T13:00:00&end_date_local=2020-09-01T23:00:00&radius_in_meters=30000&requested_seats=1&count=100&sort=departure_datetime:asc");
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_departureCoordinates_arrivalCoordinates_departureDate_and_arrivalDate_as_null_returns_valid_path() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setArrivalDate(null);

        String result = classUnderTest.buildJourneyCoordinatesPathWith(testData.build());

        assertThat(result).isEqualTo("/api/v3/trips?key=7f529ec36ab542b78e63f5270a621837&from_coordinate=52.526455,13.367701&to_coordinate=53.553918,10.005147&from_country=DE&to_country=DE&locale=de_DE&currency=EUR&start_date_local=2020-09-01T13:00:00&radius_in_meters=30000&requested_seats=1&count=100&sort=departure_datetime:asc");
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_pathVariable_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_apiVersion_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setApiVersion(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_journeyPathVariable_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setJourneyPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_authorization_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setAuthorization(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_departureCoordinates_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setDepartureCoordinates(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_arrivalCoordinates_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setArrivalCoordinates(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_country_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setCountry(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_language_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setLanguage(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_currency_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setCurrency(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_departureDate_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setDepartureDate(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_radius_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setRadius(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_numberOfPersons_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setNumberOfPersons(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_resultLength_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setResultLength(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyCoordinatesPathWith_apiToken_and_sortDirection_as_null_throws_nullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBlaBlaCarApiTokenAndUrl());
        testData.setSortDirection(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyCoordinatesPathWith(testData.build()));
    }

}
