package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother;
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
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7);
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        CallStatus<String> result = classUnderTest.getIdFrom(testResult.getBody());

        Assertions.assertThat(result.getCalledObject()).isEqualTo("981068999");
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id_when_extId_is_null() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasKarlsruheTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        CallStatus<String> result = classUnderTest.getIdFrom(testResult.getBody());

        Assertions.assertThat(result.getCalledObject()).isEqualTo("008000191");
    }

    @Test
    void test_getJourneysFrom_with_json_body_travelProvider_and_mocked_priceMapper_return_correct_journey() {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney();
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(expectedJourney, "price", "legs");
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_length() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().size()).isEqualTo(6);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_eiderstrasse_and_gartenstrasse() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_2);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(PriceObjectMother.getDBPrice());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2);

        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "price");
        Assertions.assertThat(legResult.getPrice()).isEqualToComparingFieldByField(expectedLeg.getPrice());
        Assertions.assertThat(legResult.getTravelLine()).isNull();
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_gartenstrasse_and_rendsburg_zob() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_3);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_3);

        Assertions.assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(5);
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(0));
        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_rendsburg_and_hamburgHbf() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_5);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_5);

        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
        Assertions.assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(4);
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().get(2)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(2));
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_hamburgHbf_and_frankfurtHbf() {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_6);
        HafasPriceMapper priceMapper = Mockito.mock(HafasPriceMapper.class);
        Mockito.when(priceMapper.map(Mockito.any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_6);

        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
        Assertions.assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(3);
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(0));
    }
}
