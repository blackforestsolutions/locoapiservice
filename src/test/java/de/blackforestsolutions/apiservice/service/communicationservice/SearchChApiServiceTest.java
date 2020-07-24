package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SearchChApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService = new SearchChHttpCallBuilderServiceImpl();

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final UuidService uuidService = mock(UuidService.class);

    private final SearchChMapperService searchChMapperService = spy(new SearchChMapperServiceImpl(uuidService));

    @InjectMocks
    private final SearchChApiService classUnderTest = new SearchChApiServiceImpl(callService, searchChHttpCallBuilderService, searchChMapperService);

    @BeforeEach
    void init() throws Exception {
        when(uuidService.createUUID()).thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3);
        when(searchChMapperService.getIdFromStation(getResourceFileAsString("json/searchChTestStationWithId.json")))
                .thenReturn("12345")
                .thenReturn("98765");
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getSearchChTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getSearchChTokenAndUrl();
        //noinspection unchecked
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getSearchChTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(RuntimeException.class);
    }
}
