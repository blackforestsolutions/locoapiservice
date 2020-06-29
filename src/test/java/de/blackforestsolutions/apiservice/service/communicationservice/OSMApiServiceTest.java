package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OSMApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final OSMHttpCallBuilderService osmHttpCallBuilderService = new OSMHttpCallBuilderServiceImpl();

    private final OSMApiService classUnderTest = new OSMApiServiceImpl(callService, osmHttpCallBuilderService);

    @Test
    void test_getCoordinatesFromTavelpointWith_mocked_json_apiToken_return_correct_departure_coordinates() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl();
        String departureJson = getResourceFileAsString("json/osmTravelPointDeparture.json");
        ResponseEntity<String> testResultDeparture = new ResponseEntity<>(departureJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResultDeparture).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getDeparture());
        Coordinates coordinatesResult = result.getCalledObject();

        Assertions.assertEquals(48.80549925, coordinatesResult.getLatitude());
        Assertions.assertEquals(9.228576954173775, coordinatesResult.getLongitude());
    }

    @Test
    void test_getCoordinatesFromTavelpointWith_mocked_json_apiToken_return_correct_arrival_coordinates() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl();
        String arrivalJson = getResourceFileAsString("json/osmTravelPointArrival.json");
        ResponseEntity<String> testResultArrival = new ResponseEntity<>(arrivalJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResultArrival).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getArrival());
        Coordinates coordinatesResult = result.getCalledObject();

        Assertions.assertEquals(48.0510888, coordinatesResult.getLatitude());
        Assertions.assertEquals(8.2073542, coordinatesResult.getLongitude());
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getOSMApiTokenAndUrl());
        testData.setHost(null);

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData.build(), testData.getArrival());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getOSMApiTokenAndUrl();
        //noinspection unchecked
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getArrival());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getOSMApiTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getArrival());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_wrong_pojo_returns__one_problem_with_nullPointerException() {
        ApiTokenAndUrlInformation testData = getOSMApiTokenAndUrl();
        String arrivalJson = getResourceFileAsString("json/osmTravelPointArrivalError.json");
        ResponseEntity<String> testResultArrival = new ResponseEntity<>(arrivalJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResultArrival).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Coordinates> result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getArrival());

        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
        assertThat(result.getException()).isInstanceOf(Exception.class);
    }
}
