package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.HafasJourneyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HafasMapperServiceTest {

    private final UuidService uuidGenerator = mock(UuidService.class);

    @InjectMocks
    private final HafasMapperService classUnderTest = new HafasMapperServiceImpl(uuidGenerator);

    @BeforeEach
    void init() {
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7);
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id() throws JsonProcessingException {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        String result = classUnderTest.getIdFrom(testResult.getBody());

        assertThat(result).isEqualTo("981068999");
    }

    @Test
    void test_getIdFrom_with_json_body_returns_correct_id_when_extId_is_null() throws JsonProcessingException {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasKarlsruheTravelPoint.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);

        String result = classUnderTest.getIdFrom(testResult.getBody());

        assertThat(result).isEqualTo("008000191");
    }

    @Test
    void test_getJourneysFrom_with_json_body_travelProvider_and_mocked_priceMapper_return_correct_journey() throws JsonProcessingException {
        String scheduledResourcesJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        Journey expectedJourney = getEiderstrasseRendsburgToKarlsruheJourney();
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);
        when(priceMapper.map(any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(expectedJourney, "price", "legs");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_length() throws JsonProcessingException {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);
        when(priceMapper.map(any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);

        //noinspection OptionalGetWithoutIsPresent
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().size()).isEqualTo(6);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_eiderstrasse_and_gartenstrasse() throws JsonProcessingException {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_2);
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);
        when(priceMapper.map(any())).thenReturn(PriceObjectMother.getDBPrice());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2);

        assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "price");
        assertThat(legResult.getPrice()).isEqualToComparingFieldByField(expectedLeg.getPrice());
        assertThat(legResult.getTravelLine()).isNull();
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_gartenstrasse_and_rendsburg_zob() throws JsonProcessingException {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_3);
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_3);

        assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(5);
        assertThat(legResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(0));
        assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_rendsburg_and_hamburgHbf() throws JsonProcessingException {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_5);
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);
        when(priceMapper.map(any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_5);

        assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
        assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(4);
        assertThat(legResult.getTravelLine().getBetweenHolds().get(2)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(2));
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_hamburgHbf_and_frankfurtHbf() throws JsonProcessingException {
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        Leg expectedLeg = getEiderstrasseRendsburgToKarlsruheJourney().getLegs().get(TEST_UUID_6);
        HafasPriceMapper priceMapper = mock(HafasPriceMapper.class);
        when(priceMapper.map(any())).thenReturn(new Price.PriceBuilder().build());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(testResult.getBody(), TravelProvider.DB, priceMapper);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_6);

        assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
        assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(3);
        assertThat(legResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(0));
    }

    @Test
    void test_getJourneysFrom_with_wrong_pojo_returns_problem_with_nullPointerException() {
        String json = getResourceFileAsString("json/dbHafasJourney.json");
        HafasJourneyResponse testData = retrieveJsonToPojo(json, HafasJourneyResponse.class);
        testData.getSvcResL().get(0).getRes().getOutConL().get(0).setDate(null);
        HafasPriceMapper priceMapper = (price) -> new Price.PriceBuilder().build();

        Map<UUID, JourneyStatus> result = ReflectionTestUtils.invokeMethod(classUnderTest, "getJourneysFrom", testData, TravelProvider.DB, priceMapper);

        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(Exception.class);
    }
}
