package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperService;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.LufthansaHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LufthansaApiServiceTest {

    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder, WebClient.create());

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final UuidService uuidService = mock(UuidService.class);

    private final LufthansaMapperService mapper = new LufthansaMapperServiceImpl(airportConfiguration.airports(), uuidService);

    private final LuftHansaHttpCallBuilderService httpCallBuilderService = new LufthansaHttpCallBuilderServiceImpl();

    private final LufthansaApiService classUnderTest = new LufthansaApiServiceImpl(callService, httpCallBuilderService, mapper);

    LufthansaApiServiceTest() throws IOException {
    }

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7)
                .thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9)
                .thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11)
                .thenReturn(TEST_UUID_12)
                .thenReturn(TEST_UUID_13)
                .thenReturn(TEST_UUID_14)
                .thenReturn(TEST_UUID_15)
                .thenReturn(TEST_UUID_16)
                .thenReturn(TEST_UUID_17)
                .thenReturn(TEST_UUID_18)
                .thenReturn(TEST_UUID_19)
                .thenReturn(TEST_UUID_20)
                .thenReturn(TEST_UUID_21)
                .thenReturn(TEST_UUID_22);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        testData.setArrivalDate(ZonedDateTime.now());
        testData.setDepartureDate(ZonedDateTime.now());
        String scheduledResourcesJson = getResourceFileAsString("json/lufthansaJourneyTest.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData.build()).getCalledObject();

        assertThat("E90").isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getVehicleNumber());
        assertThat(TravelProvider.LUFTHANSA).isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getTravelProvider());
        assertThat("LH1191").isEqualTo(result.get(TEST_UUID_11).getJourney().get().getLegs().get(TEST_UUID_12).getProviderId());
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getLufthansaTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getLufthansaTokenAndUrl();
        //noinspection unchecked
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getLufthansaTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(RuntimeException.class);
    }
}

