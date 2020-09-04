package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperService;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class AirportsFinderApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final CallService callService = spy(new CallServiceImpl(restTemplateBuilder, WebClient.create()));

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService mapper = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    private final AirportsFinderHttpCallBuilderService httpCallBuilderService = new AirportsFinderHttpCallBuilderServiceImpl();

    private final AirportsFinderApiService classUnderTest = new AirportsFinderApiServiceImpl(callService, httpCallBuilderService, mapper);

    AirportsFinderApiServiceTest() throws IOException {
    }

    @Test
    void test_getAirportsAsTravelPoints_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_linkedHashSet() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        List<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getAirportsFinderTokenAndUrl();
        ResponseEntity<String> testResult = new ResponseEntity<>(airportsFinderResource, HttpStatus.OK);
        //noinspection unchecked
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(apiTokenAndUrlInformation);

        assertThat(new ArrayList<>(result.getCalledObject()).size()).isEqualTo(3);
        assertThat(new ArrayList<>(result.getCalledObject()).get(0).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        assertThat(new ArrayList<>(result.getCalledObject()).get(1).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(1));
        assertThat(new ArrayList<>(result.getCalledObject()).get(2).getTravelPoint().isEmpty()).isTrue();
    }

    @Test
    void test_getAirportsWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getAirportsFinderTokenAndUrl());
        testData.setHost(null);

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getAirportsWith_apiToken_and_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getAirportsFinderTokenAndUrl();
        //noinspection unchecked
        doReturn(new ResponseEntity<>("", HttpStatus.UNAUTHORIZED)).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getAirportsWith_apiToken_and_mocked_empty_http_answer_returns_empty_map() {
        ApiTokenAndUrlInformation testData = getAirportsFinderTokenAndUrl();
        //noinspection unchecked
        doReturn(new ResponseEntity<>("[]", HttpStatus.OK)).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getCalledObject().size()).isEqualTo(0);
    }

    @Test
    void test_getAirportsWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getAirportsFinderTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(RuntimeException.class);
    }

}