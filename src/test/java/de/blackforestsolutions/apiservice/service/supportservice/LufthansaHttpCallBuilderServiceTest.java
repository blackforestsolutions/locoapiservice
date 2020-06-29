package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl;

class LufthansaHttpCallBuilderServiceTest {

    private final LuftHansaHttpCallBuilderService classUnderTest = new LufthansaHttpCallBuilderServiceImpl();

    @Test
    void test_buildLufthansaJourneyPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getLufthansaTokenAndUrl();

        String result = classUnderTest.buildLufthansaJourneyPathWith(testData);

        Assertions.assertEquals(result, "/v1/operations/schedules/ZRH/FRA/2019-06-28");
    }

    @Test
    void test_buildLufthansaAuthorizationPathWith_with_apiVersion_pathVariable_returns_valid_path() {
        ApiTokenAndUrlInformation testData = getLufthansaTokenAndUrl();

        String result = classUnderTest.buildLufthansaAuthorizationPathWith(testData);

        Assertions.assertEquals(result, "/v1/oauth/token");
    }

    @Test
    void test_buildHttpEntityForLufthansaAuthorization_with_clientId_clientSecret_clientType_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = getLufthansaTokenAndUrl();

        HttpEntity<MultiValueMap<String, String>> result = classUnderTest.buildHttpEntityForLufthansaAuthorization(testData);

        Assertions.assertEquals(new HttpHeaders(), result.getHeaders());
        Assertions.assertEquals(HttpBodyObjectMother.getLufthansaAuthorizationBody(), result.getBody());
    }

    @Test
    void test_buildPathWith_apiVersion_as_null_pathVariable_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        builder.setApiVersion(null);

        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLufthansaJourneyPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_apiVersion_journeyPathVariable_as_null_departure_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        builder.setJourneyPathVariable(null);

        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLufthansaJourneyPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_as_null_arrival_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        builder.setDeparture(null);

        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLufthansaJourneyPathWith(builder.build()));
    }

    @Test
    void test_buildPathWith_apiVersion_pathvariable_departure_arrival_as_null_departureDate_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        builder.setArrival(null);

        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildLufthansaJourneyPathWith(builder.build()));
    }
}
