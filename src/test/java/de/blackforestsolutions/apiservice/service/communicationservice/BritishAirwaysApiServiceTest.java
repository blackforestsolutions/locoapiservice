package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BritishAirwaysCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BritishAirwaysCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperService;
import de.blackforestsolutions.apiservice.service.mapper.BritishAirwaysMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BritishAirwaysHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class BritishAirwaysApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final BritishAirwaysCallService britishAirwaysCallService = new BritishAirwaysCallServiceImpl(restTemplateBuilder);

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final BritishAirwaysHttpCallBuilderService britishAirwaysHttpCallBuilderService = new BritishAirwaysHttpCallBuilderServiceImpl();

    private final UuidService mockedUuidService = mock(UuidService.class);

    private final BritishAirwaysMapperService britishAirwaysMapperService = new BritishAirwaysMapperServiceImpl(airportConfiguration.airports(), mockedUuidService);

    @InjectMocks
    private final BritishAirwaysApiService classUnderTest = new BritishAirwaysApiServiceImpl(britishAirwaysCallService, britishAirwaysHttpCallBuilderService, britishAirwaysMapperService);

    BritishAirwaysApiServiceTest() throws IOException {
    }

    private Date buildDateFrom(String dateTime) throws ParseException {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'mm:ss");
        return inFormat.parse(dateTime);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test1_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrlIT();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        Date now = TestUtils.formatDate(new Date());
        builder.setArrivalDate(now);
        builder.setDepartureDate(now);
        apiTokenAndUrlInformation = builder.build();
        String scheduledResourcesJson = TestUtils.getResourceFileAsString("json/BritishAirwaysJsons/1_britishAirways_lhr_txl.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), Mockito.any(), any(Class.class));
        Mockito.when(mockedUuidService.createUUID()).thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3).thenReturn(TEST_UUID_4);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getTravelProvider());
        Assertions.assertThat("0982").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getProviderId());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T08:45:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T11:35:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T08:56:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2019-10-20T11:32:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("319").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_1).getJourney().get().isMatchesRequest());


        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getTravelProvider());
        Assertions.assertThat("0992").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getProviderId());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T10:50:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T13:45:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T10:47:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2019-10-20T13:36:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("319").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_2).getJourney().get().isMatchesRequest());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getTravelProvider());
        Assertions.assertThat("0984").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getProviderId());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T12:55:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:50:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T12:54:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:39:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("321").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_3).getJourney().get().isMatchesRequest());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getTravelProvider());
        Assertions.assertThat("0986").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getProviderId());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Actual").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStart().getCity());
        Assertions.assertThat("TXL").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("Berlin").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStart().getTerminal());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:30:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T18:25:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2019-10-20T15:28:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2019-10-20T18:13:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("32A").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_4).getJourney().get().isMatchesRequest());
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test2_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBritishAirwaysTokenAndUrlIT();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        Date now = TestUtils.formatDate(new Date());
        builder.setArrivalDate(now);
        builder.setDepartureDate(now);
        apiTokenAndUrlInformation = builder.build();
        String scheduledResourcesJson = TestUtils.getResourceFileAsString("json/BritishAirwaysJsons/3_britishAirways_lhr_jfk.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));
        Mockito.when(mockedUuidService.createUUID()).thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3).thenReturn(TEST_UUID_4);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getTravelProvider());
        Assertions.assertThat("0117").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getProviderId());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getTerminal());
        Assertions.assertThat("7").isEqualTo((result.get(TEST_UUID_1).getJourney().get().getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T08:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T08:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("744").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_1).getJourney().get().isMatchesRequest());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getTravelProvider());
        Assertions.assertThat("1516").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getProviderId());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDestination().getCity());
        Assertions.assertThat("3").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getTerminal());
        Assertions.assertThat("8").isEqualTo((result.get(TEST_UUID_2).getJourney().get().getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T10:10:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T13:25:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T10:10:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2020-01-11T13:25:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("AA").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("772").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_2).getJourney().get().isMatchesRequest());


        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getTravelProvider()); //datamodel
        Assertions.assertThat("0173").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getProviderId());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDestination().getCity());
        Assertions.assertThat("5").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getTerminal());
        Assertions.assertThat("7").isEqualTo((result.get(TEST_UUID_3).getJourney().get().getDestination().getTerminal()));
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:25:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T11:25:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:25:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("BA").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("744").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_3).getJourney().get().isMatchesRequest());

        Assertions.assertThat(TravelProvider.BRITISHAIRWAYS).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getTravelProvider());
        Assertions.assertThat("1506").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getProviderId());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalStatus());
        Assertions.assertThat("Planned").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartStatus());
        Assertions.assertThat("LHR").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStart().getAirportId());
        Assertions.assertThat("London").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStart().getCity());
        Assertions.assertThat("JFK").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getDestination().getAirportId());
        Assertions.assertThat("New York").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getDestination().getCity());
        Assertions.assertThat("8").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getDestination().getTerminal());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:45:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T18:10:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalTime());
        Assertions.assertThat(buildDateFrom("2020-01-11T14:45:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getStartTimeUpdated());
        Assertions.assertThat(buildDateFrom("2020-01-11T18:10:00")).isEqualTo(result.get(TEST_UUID_4).getJourney().get().getArrivalTimeUpdated());
        Assertions.assertThat("AA").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getUnknownTravelProvider());
        Assertions.assertThat("772").isEqualTo(result.get(TEST_UUID_4).getJourney().get().getVehicleNumber());
        Assertions.assertThat(true).isEqualTo(result.get(TEST_UUID_4).getJourney().get().isMatchesRequest());
    }
}
