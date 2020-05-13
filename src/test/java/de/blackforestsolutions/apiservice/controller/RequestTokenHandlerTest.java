package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestTokenHandlerTest {

    @Test
    void test_getRequestApiTokenWith_two_token_returns_correct_call_token() {
        //arrange
        ApiTokenAndUrlInformation configured = ApiTokenAndUrlInformationObjectMother.configurationToken();
        ApiTokenAndUrlInformation requestInfos = ApiTokenAndUrlInformationObjectMother.requestInfos();
        //act
        ApiTokenAndUrlInformation result = RequestTokenHandler.getRequestApiTokenWith(requestInfos, configured);
        //assert
        Assertions.assertThat(result.getHost()).isEqualTo(configured.getHost());
        Assertions.assertThat(result.getProtocol()).isEqualTo(configured.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(configured.getPort());
        Assertions.assertThat(result.getPathVariable()).isEqualTo(configured.getPathVariable());
        Assertions.assertThat(result.getLocationPath()).isEqualTo(configured.getLocationPath());
        Assertions.assertThat(result.getGermanRailJourneyDeatilsPath()).isEqualTo(configured.getGermanRailJourneyDeatilsPath());
        Assertions.assertThat(result.getAuthorizationKey()).isEqualTo(configured.getAuthorizationKey());
        Assertions.assertThat(result.getAuthorization()).isEqualTo(configured.getAuthorization());
        Assertions.assertThat(result.getDeparture()).isEqualTo(requestInfos.getDeparture());
        Assertions.assertThat(result.getArrival()).isEqualTo(requestInfos.getArrival());
        Assertions.assertThat(result.getArrivalDate()).isEqualTo(requestInfos.getArrivalDate());
        Assertions.assertThat(result.getDepartureDate()).isEqualTo(requestInfos.getDepartureDate());
    }
}
