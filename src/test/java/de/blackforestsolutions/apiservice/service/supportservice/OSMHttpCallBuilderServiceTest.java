package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OSMHttpCallBuilderServiceTest {

    private final OSMHttpCallBuilderService classUnderTest = new OSMHttpCallBuilderServiceImpl();

    @Test
    void test_buildOSMPathWith_pathvariable_outputformat_departure_arrival_returns_correct_departure_url() {
        // Arrange
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl();

        // Act
        String result = classUnderTest.buildOSMPathWith(testData, testData.getArrival());

        // Assert
        Assertions.assertThat(result).isEqualTo("/?addressdetails=1&q=Stuttgart,+Waiblinger+Str.+84&format=json&limit=1");
    }

    @Test
    void test_buildOSMPathWith_pathvariable_outputformat_departure_arrival_returns_correct_arrival_url() {
        // Arrange
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl();

        // Act
        String result = classUnderTest.buildOSMPathWith(testData, testData.getDeparture());

        // Assert
        Assertions.assertThat(result).isEqualTo("/?addressdetails=1&q=Stuttgart,+Waiblinger+Str.+84&format=json&limit=1");
    }
}
