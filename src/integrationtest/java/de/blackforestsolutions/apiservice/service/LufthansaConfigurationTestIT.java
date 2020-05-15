package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.configuration.LufthansaConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import javax.annotation.Resource;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getEmptyApiToken;
import static de.blackforestsolutions.apiservice.objectmothers.CallStatusObjectMother.getLufthansaAuthorizationResponse;
import static org.mockito.Mockito.*;

@SpringBootTest
class LufthansaConfigurationTestIT {

    @MockBean
    private LufthansaApiService lufthansaApiService;

    @SpyBean
    private LufthansaConfiguration classUnderTest;

    @Resource(name = "lufthansaApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation lufthansaApiTokenAndUrlInformation;

    @BeforeEach
    void init() {
        when(lufthansaApiService.getLufthansaAuthorization(getEmptyApiToken()))
                .thenReturn(getLufthansaAuthorizationResponse());
    }

    @Test
    void test_getBearerTokenForLufthansa_is_called_when_application_starts() {
        Awaitility.await()
                .atMost(Duration.ONE_SECOND)
                .untilAsserted(() -> verify(classUnderTest, times(1)).getBearerTokenForLufthansa());
    }

    @Test
    void test_getBearerTokenForLufthansa_is_called_when_application_starts_updates_apiToken() {
        Awaitility.await()
                .atMost(Duration.TEN_SECONDS)
                .untilAsserted(() -> Assertions.assertThat(lufthansaApiTokenAndUrlInformation.getAuthorization()).isNotNull());
    }
}
