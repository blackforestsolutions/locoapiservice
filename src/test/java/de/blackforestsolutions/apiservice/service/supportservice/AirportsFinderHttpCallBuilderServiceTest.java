package de.blackforestsolutions.apiservice.service.supportservice;


import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl;

class AirportsFinderHttpCallBuilderServiceTest {
    private final AirportsFinderHttpCallBuilderService classUnderTest = new AirportsFinderHttpCallBuilderServiceImpl();

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getAirportsFinderTokenAndUrl();

        String result = classUnderTest.buildPathWith(testData);

        org.junit.jupiter.api.Assertions.assertEquals(result, "/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }

    @Test
    void test_buildHttpHeadersForAirportsFinderWith_AirportsFinderTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getAirportsFinderTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpEntityAirportsFinder(apiTokenAndUrlInformation).getHeaders();

        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildHttpEntityAirportsFinder_with_apiToken_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = getAirportsFinderTokenAndUrl();

        HttpEntity<String> result = classUnderTest.buildHttpEntityAirportsFinder(testData);

        Assertions.assertThat(result.getHeaders()).containsKeys(testData.getAuthorizationKey());
        Assertions.assertThat(result.hasBody()).isFalse();
    }

    @Test
    void test_buildPathWith_apiVersion_pathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getAirportsFinderTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_everything_returns_correct_path() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getAirportsFinderTokenAndUrl());

        String result = classUnderTest.buildPathWith(builder.build());

        Assertions.assertThat(result).isEqualTo("/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
    }
}
