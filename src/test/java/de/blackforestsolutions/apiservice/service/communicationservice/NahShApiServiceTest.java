package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperService;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NahShApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final HafasCallService hafasCallService = new HafasCallServiceImpl(restTemplateBuilder);

    private final HafasHttpCallBuilderService hafasHttpCallBuilderService = new HafasHttpCallBuilderServiceImpl();

    private final UuidService uuidGenerator = mock(UuidService.class);

    private final HafasMapperService hafasMapperService = new HafasMapperServiceImpl(uuidGenerator);

    private final HafasApiService hafasApiService = new HafasApiServiceImpl(hafasCallService, hafasHttpCallBuilderService, hafasMapperService);

    private final NahShApiService classUnderTest = new NahShApiServiceImpl(hafasApiService);

    @BeforeEach
    void init() {
        String scheduledResourceJson = getResourceFileAsString("json/nahShHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        String travelPointResourceJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> travelPointResult = new ResponseEntity<>(travelPointResourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(travelPointResult)
                .thenReturn(travelPointResult)
                .thenReturn(testResult);
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_journey() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(journeyResult.getBetweenTrips().size()).isEqualTo(3);
        Assertions.assertThat(journeyResult.getStartTime()).isEqualTo(expectedJourney.getStartTime());
        Assertions.assertThat(journeyResult.getArrivalTime()).isEqualTo(expectedJourney.getArrivalTime());
        Assertions.assertThat(journeyResult.getDuration()).isEqualTo(expectedJourney.getDuration());
        Assertions.assertThat(journeyResult.getTravelProvider()).isEqualTo(expectedJourney.getTravelProvider());
        Assertions.assertThat(journeyResult).isEqualToIgnoringGivenFields(expectedJourney, "start", "destination", "price", "betweenTrips");
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_start_and_destination() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent,OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(journeyResult.getStart()).isEqualToComparingFieldByField(expectedJourney.getStart());
        Assertions.assertThat(journeyResult.getDestination()).isEqualToComparingFieldByField(expectedJourney.getDestination());
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_price() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(journeyResult.getPrice()).isEqualToComparingFieldByField(expectedJourney.getPrice());
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_eiderstrasse_and_gartenstrasse() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney().getBetweenTrips().get(0);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0);

        Assertions.assertThat(betweenTripResult).isEqualToComparingFieldByField(expectedBetweenTrip);
        Assertions.assertThat(betweenTripResult.getTravelLine()).isNull();
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_gartenstrasse_and_rendsburg_zob() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney().getBetweenTrips().get(1);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(1);

        Assertions.assertThat(betweenTripResult).isEqualToIgnoringGivenFields(expectedBetweenTrip, "travelLine");
        Assertions.assertThat(betweenTripResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedBetweenTrip.getTravelLine(), "betweenHolds");
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().size()).isEqualTo(5);
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedBetweenTrip.getTravelLine().getBetweenHolds().get(0));
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_rendsburg_zob_and_rendsburg() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney().getBetweenTrips().get(2);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(2);

        Assertions.assertThat(betweenTripResult).isEqualToComparingFieldByField(expectedBetweenTrip);
        Assertions.assertThat(betweenTripResult.getTravelLine()).isNull();
    }
}
