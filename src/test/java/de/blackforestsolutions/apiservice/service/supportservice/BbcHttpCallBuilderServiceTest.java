package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

class BbcHttpCallBuilderServiceTest {

    private final BBCHttpCallBuilderService classUnderTest = new BBCHttpCallBuilderServiceImpl();

    @Test
    void test_bbc_buildPathWith_apiVersion_pathvariable_departure_arrival_departureDate_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        String result = classUnderTest.bbcBuildPathWith(testData);

        Assertions.assertThat(result).isEqualTo("/api/v2/trips?fn=ZRH&tn=FRA&db=2019-12-28");
    }

    @Test
    void test_buildHttpHeadersForBbcWith_BbcTokenAndUrlInfo_returns_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        HttpHeaders result = classUnderTest.buildHttpHeadersForBbcWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey()).get(0)).isEqualTo(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildHttpEntityForBbc_with_apiToken_returns_correct_httpEntity() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();

        HttpEntity<String> result = classUnderTest.buildHttpEntityForBbc(testData);

        Assertions.assertThat(result.getHeaders()).containsKeys(testData.getAuthorizationKey());
        Assertions.assertThat(result.hasBody()).isFalse();
    }
}
