package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;

class HafasMapperServiceTest {

    private final UuidService uuidGenerator = Mockito.mock(UuidService.class);

    @InjectMocks
    private final HafasMapperService classUnderTest = new HafasMapperServiceImpl(uuidGenerator);

    @BeforeEach
    void init() {
        Mockito.when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(UUID.randomUUID())
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        CallStatus result = classUnderTest.getIdFrom(testResult.getBody());
        String callResult = (String) result.getCalledObject();

        Assertions.assertThat(callResult).isEqualTo("981068999");
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id_when_extId_is_null() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasKarlsruheTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        CallStatus result = classUnderTest.getIdFrom(testResult.getBody());
        String callResult = (String) result.getCalledObject();

        Assertions.assertThat(callResult).isEqualTo("008000191");
    }

    @Test
    void test_getJourneysFrom_with_json_body_travelProvider_and_mocked_priceMapper_return_correct_journey() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        Journey expectedValue = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney();
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(expectedValue, "betweenTrips", "price");
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_betweenTrips_length() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().size()).isEqualTo(6);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_eiderstrasse_and_gartenstrasse() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getBetweenTrips().get(0);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0);

        Assertions.assertThat(betweenTripResult).isEqualToComparingFieldByField(expectedBetweenTrip);
        Assertions.assertThat(betweenTripResult.getTravelLine()).isNull();
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_gartenstrasse_and_rendsburg_zob() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getBetweenTrips().get(1);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(1);

        Assertions.assertThat(betweenTripResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedBetweenTrip.getTravelLine(), "betweenHolds");
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().size()).isEqualTo(5);
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedBetweenTrip.getTravelLine().getBetweenHolds().get(0));
        Assertions.assertThat(betweenTripResult).isEqualToIgnoringGivenFields(expectedBetweenTrip, "travelLine");
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_rendsburg_and_hamburgHbf() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getBetweenTrips().get(2);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(3);

        Assertions.assertThat(betweenTripResult).isEqualToIgnoringGivenFields(expectedBetweenTrip, "travelLine");
        Assertions.assertThat(betweenTripResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedBetweenTrip.getTravelLine(), "betweenHolds");
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().size()).isEqualTo(4);
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().get(2)).isEqualToComparingFieldByField(expectedBetweenTrip.getTravelLine().getBetweenHolds().get(2));
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_trip_between_hamburgHbf_and_frankfurtHbf() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Journey expectedBetweenTrip = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getBetweenTrips().get(3);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Journey betweenTripResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(4);

        Assertions.assertThat(betweenTripResult).isEqualToIgnoringGivenFields(expectedBetweenTrip, "travelLine");
        Assertions.assertThat(betweenTripResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedBetweenTrip.getTravelLine(), "betweenHolds");
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().size()).isEqualTo(3);
        Assertions.assertThat(betweenTripResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedBetweenTrip.getTravelLine().getBetweenHolds().get(0));
    }
}
