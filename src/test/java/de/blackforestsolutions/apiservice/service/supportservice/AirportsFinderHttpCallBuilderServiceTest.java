package de.blackforestsolutions.apiservice.service.supportservice;


import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

class AirportsFinderHttpCallBuilderServiceTest {
    private final AirportsFinderHttpCallBuilderService classUnderTest = new AirportsFinderHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        String result = classUnderTest.buildPathWith(testData);

        org.junit.jupiter.api.Assertions.assertEquals(result, "/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }

    @Test
    void test_buildHttpHeadersForAirportsFinderWith_AirportsFinderTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpEntityAirportsFinder(apiTokenAndUrlInformation).getHeaders();

        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildHttpEntityAirportsFinder_with_apiToken_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();

        HttpEntity<String> result = classUnderTest.buildHttpEntityAirportsFinder(testData);

        Assertions.assertThat(result.getHeaders()).containsKeys(testData.getAuthorizationKey());
        Assertions.assertThat(result.hasBody()).isFalse();
    }

    @Test
    void test_buildPathWith_apiVersion_pathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(finalTestData));
    }

    @Test
    void test_buildPathWith_everything_returns_correct_path() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder = builder.buildFrom(testData);
        testData = builder.build();
        ApiTokenAndUrlInformation finalTestData = testData;
        String result = classUnderTest.buildPathWith(finalTestData);

        Assertions.assertThat(result).isEqualTo("/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }
}
