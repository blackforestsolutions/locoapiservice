package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.configuration.TimeConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperService;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BritishAirwaysApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService = new BritishAirwaysHttpCallBuilderServiceImpl();

    private final UuidService mockedUuidService = mock(UuidService.class);

    private final BritishAirwaysMapperService britishAirwaysMapperService = new BritishAirwaysMapperServiceImpl(airportConfiguration.airports(), mockedUuidService);

    @InjectMocks
    private final BritishAirwaysApiService classUnderTest = new BritishAirwaysApiServiceImpl(callService, britishAirwaysHttpCallBuilderService, britishAirwaysMapperService);

    BritishAirwaysApiServiceTest() throws IOException {
    }

    private ZonedDateTime buildDateFrom(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
    }

    @BeforeEach
    void init() {
        Mockito.when(mockedUuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7)
                .thenReturn(TEST_UUID_8);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test1_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        testData.setArrivalDate(ZonedDateTime.now());
        testData.setDepartureDate(ZonedDateTime.now());
        String scheduledResourcesJson = TestUtils.getResourceFileAsString("json/BritishAirwaysJsons/1_britishAirways_lhr_txl.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), Mockito.any(), any(Class.class));

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData.build()).getCalledObject();

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getTravelProvider());
        Assertions.assertThat("0982").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getStationId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T08:56:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStartTime()); // start time updated has been removed - start and arrival time provide the updated times
        Assertions.assertThat(buildDateFrom("2019-10-20T11:32:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getUnknownTravelProvider());
        Assertions.assertThat("319").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getVehicleNumber());


        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getTravelProvider());
        Assertions.assertThat("0992").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getStationId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T10:47:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T13:36:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getUnknownTravelProvider());
        Assertions.assertThat("319").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getVehicleNumber());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getTravelProvider());
        Assertions.assertThat("0984").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getStationId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T12:54:00")).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:39:00")).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getUnknownTravelProvider());
        Assertions.assertThat("321").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getVehicleNumber());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getTravelProvider());
        Assertions.assertThat("0986").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getStationId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:28:00")).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T18:13:00")).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getUnknownTravelProvider());
        Assertions.assertThat("32A").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getVehicleNumber());
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test2_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        testData.setArrivalDate(ZonedDateTime.now());
        testData.setDepartureDate(ZonedDateTime.now());
        String scheduledResourcesJson = getResourceFileAsString("json/BritishAirwaysJsons/3_britishAirways_lhr_jfk.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData.build()).getCalledObject();

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getTravelProvider());
        Assertions.assertThat("0117").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getStationId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getTerminal());
        Assertions.assertThat("7").isEqualTo((result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T08:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getUnknownTravelProvider());
        Assertions.assertThat("744").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getVehicleNumber());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getTravelProvider());
        Assertions.assertThat("1516").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getStationId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getCity());
        Assertions.assertThat("3").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getTerminal());
        Assertions.assertThat("8").isEqualTo((result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T10:10:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T13:25:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getArrivalTime());
        Assertions.assertThat("AA").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getUnknownTravelProvider());
        Assertions.assertThat("772").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getVehicleNumber());


        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getTravelProvider()); //datamodel
        Assertions.assertThat("0173").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getStationId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getTerminal());
        Assertions.assertThat("7").isEqualTo((result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:25:00")).isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getArrivalTime());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getUnknownTravelProvider());
        Assertions.assertThat("744").isEqualTo(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getVehicleNumber());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getTravelProvider());
        Assertions.assertThat("1506").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getProviderId());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getStationId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getStationId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getCity());
        Assertions.assertThat("8").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getTerminal());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:45:00")).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T18:10:00")).isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getArrivalTime());
        Assertions.assertThat("AA").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getUnknownTravelProvider());
        Assertions.assertThat("772").isEqualTo(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getVehicleNumber());
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBritishAirwaysTokenAndUrl());
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getBritishAirwaysTokenAndUrl();
        //noinspection unchecked
        doReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST)).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(MismatchedInputException.class);
    }

    @Test
    void test_getJourneysForRouteWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getBritishAirwaysTokenAndUrl();
        //noinspection unchecked
        doThrow(new RuntimeException()).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getException()).isInstanceOf(RuntimeException.class);
    }

}
