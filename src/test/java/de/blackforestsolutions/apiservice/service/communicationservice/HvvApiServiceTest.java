package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.service.supportservice.UuidServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HvvApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final HvvHttpCallBuilderService hvvHttpCallBuilderService = new HvvHttpCallBuilderServiceImpl();

    private final UuidService uuidService = new UuidServiceImpl();

    private final HvvMapperService mapperService = spy(new HvvMapperServiceImpl(uuidService));

    @InjectMocks
    private HvvApiService classUnderTest = new HvvApiServiceImpl(callService, hvvHttpCallBuilderService, mapperService);

    @Test
    void test_getJourneysForRouteFromHvvApiWith_with_mocked_rest_and_json_is_excuted_correctly_and_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        String rotenhofTravelPointJson = TestUtils.getResourceFileAsString("json/hvvRotenhofTravelPoint.json");
        ResponseEntity<String> rotenhofTravelPointResult = new ResponseEntity<>(rotenhofTravelPointJson, HttpStatus.OK);
        String stadthausbrueckeTravelPointJson = TestUtils.getResourceFileAsString("json/hvvStadthausbrueckeTravelPoint.json");
        ResponseEntity<String> stadthausbrueckeTravelPointResult = new ResponseEntity<>(stadthausbrueckeTravelPointJson, HttpStatus.OK);
        String journeyJson = TestUtils.getResourceFileAsString("json/hvvJourney.json");
        ResponseEntity<String> journeyTestResult = new ResponseEntity<>(journeyJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(rotenhofTravelPointResult).thenReturn(stadthausbrueckeTravelPointResult).thenReturn(journeyTestResult);
        HashMap<UUID, JourneyStatus> mockedJourneys = new HashMap<>();
        Journey mockedJourney = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(mockedJourney));
        mockedJourneys.put(mockedJourney.getId(), journeyStatus);
        when(mapperService.getJourneyMapFrom(journeyJson)).thenReturn(mockedJourneys);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation).getCalledObject();

        Assertions.assertThat(result.size()).isEqualTo(1);
        //noinspection OptionalGetWithoutIsPresent (justification: we allways knoww that there optional here is not empty)
        Assertions.assertThat(result.get(mockedJourney.getId()).getJourney().get()).isEqualToComparingFieldByField(mockedJourney);
        verify(mapperService, times(1)).getJourneyMapFrom(journeyJson);
    }

    @Test
    void test_getStationListFromHvvApiWith_with_mocked_rest_is_excuted_correctly_and_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl();
        String stationListJson = TestUtils.getResourceFileAsString("json/hvvStationList.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(stationListJson, HttpStatus.OK);
        //noinspection unchecked
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));
        List<TravelPoint> mockedTravelPointsList = Arrays.asList(
                TravelPointObjectMother.getHvvHauptbahnhofTravelPoint(),
                TravelPointObjectMother.getPinnebergRichardKoehnHvvTravelPoint()
        );
        when(mapperService.getStationListFrom(stationListJson)).thenReturn(mockedTravelPointsList);

        List<TravelPoint> result = classUnderTest.getStationListFromHvvApiWith(apiTokenAndUrlInformation).getCalledObject();

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(TravelPointObjectMother.getHvvHauptbahnhofTravelPoint());
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(TravelPointObjectMother.getPinnebergRichardKoehnHvvTravelPoint());
        verify(mapperService, times(1)).getStationListFrom(stationListJson);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        //noinspection unchecked
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(RuntimeException.class);
    }

}
