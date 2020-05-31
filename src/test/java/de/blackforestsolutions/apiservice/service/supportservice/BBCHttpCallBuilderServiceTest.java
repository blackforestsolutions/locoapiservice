package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBBCTokenAndUrl;
import static org.assertj.core.api.Assertions.assertThat;

class BBCHttpCallBuilderServiceTest {

    private final BBCHttpCallBuilderService classUnderTest = new BBCHttpCallBuilderServiceImpl();

    @Test
    void test_bbc_bbcBuildJourneyStringPathWith_departure_arrival_and_departureDate_returns_valid_path() {
        ApiTokenAndUrlInformation testData = getBBCTokenAndUrl();

        String result = classUnderTest.bbcBuildJourneyStringPathWith(testData);

        assertThat(result).isEqualTo("/api/v2/trips?db=2020-05-31+13:00:00&key=7f529ec36ab542b78e63f5270a621837&radius=30&locale=de_DE&cur=EUR&seats=1&limit=100&fn=Berlin&tn=Hamburg");
    }

    @Test
    void test_bbcBuildJourneyStringPathWith_departure_arrival_arrivalDate_and_timeIsDeparture_as_null_returns_valid_path() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBBCTokenAndUrl());
        testData.setTimeIsDeparture(false);

        String result = classUnderTest.bbcBuildJourneyStringPathWith(testData.build());

        assertThat(result).isEqualTo("/api/v2/trips?de=2020-06-02+13:00:00&sort=departure_datetime:desc&key=7f529ec36ab542b78e63f5270a621837&radius=30&locale=de_DE&cur=EUR&seats=1&limit=100&fn=Berlin&tn=Hamburg");
    }

    @Test
    void test_bbcBuildJourneyCoordinatesPathWith_departureCoordinates_arrivalCoordinates_departureDate_returns_valid_path() {
        ApiTokenAndUrlInformation testData = getBBCTokenAndUrl();

        String result = classUnderTest.bbcBuildJourneyCoordinatesPathWith(testData);

        assertThat(result).isEqualTo("/api/v2/trips?db=2020-05-31+13:00:00&key=7f529ec36ab542b78e63f5270a621837&radius=30&locale=de_DE&cur=EUR&seats=1&limit=100&fc=52.526455%7C13.367701&tc=53.553918%7C10.005147");
    }

    @Test
    void test_bbcBuildJourneyCoordinatesPathWith_departureCoordinates_as_null_arrivalCoordinates_as_null_and_departureDate_returns_path_where_all_null_variables_are_replaced_by_departure_and_arrival() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBBCTokenAndUrl());
        testData.setDepartureCoordinates(null);
        testData.setArrivalCoordinates(null);

        String result = classUnderTest.bbcBuildJourneyCoordinatesPathWith(testData.build());

        assertThat(result).isEqualTo("/api/v2/trips?db=2020-05-31+13:00:00&key=7f529ec36ab542b78e63f5270a621837&radius=30&locale=de_DE&cur=EUR&seats=1&limit=100&fn=Berlin&tn=Hamburg");
    }

}
