package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.TravelLine;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;

class SearchChMapperServiceTest {

    @Mock
    private final UuidService uuidService = Mockito.mock(UuidService.class);

    @InjectMocks
    private final SearchChMapperService classUnderTest = new SearchChMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        Mockito.when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4);
    }

    @Test
    void test_getJourneysFrom_with_json_and_return_map_with_journeys() {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_journey() throws ParseException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        Journey testJourneyData = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(testJourneyData, "legs");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_legs() throws ParseException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        LinkedHashMap<UUID, Leg> testLegs = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey().getLegs();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);
        LinkedHashMap<UUID, Leg> legResult = result.get(TEST_UUID_1).getJourney().get().getLegs();

        Assertions.assertThat(legResult.get(TEST_UUID_2)).isEqualToIgnoringGivenFields(testLegs.get(TEST_UUID_2), "travelLine");
        Assertions.assertThat(legResult.get(TEST_UUID_3)).isEqualToComparingFieldByField(testLegs.get(TEST_UUID_3));
        Assertions.assertThat(legResult.get(TEST_UUID_4)).isEqualToComparingFieldByField(testLegs.get(TEST_UUID_4));
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_travelLine() throws ParseException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        TravelLine testTravelLine = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey().getLegs().get(TEST_UUID_2).getTravelLine();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);
        TravelLine travelLineResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getTravelLine();

        Assertions.assertThat(travelLineResult).isEqualToIgnoringGivenFields(testTravelLine, "betweenHolds");
        Assertions.assertThat(travelLineResult.getBetweenHolds().size()).isEqualTo(3);
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(0)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(0));
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(1)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(1), "country");
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(2)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(2));
    }

    @Test
    void test_getIdFromStation_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_withId() throws Exception {
        String jsonResources = getResourceFileAsString("json/searchChTestStationWithId.json");

        String result = classUnderTest.getIdFromStation(jsonResources);

        Assertions.assertThat(result).isEqualTo("8505000");
    }

    @Test
    void test_getIdFromStation_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_withoutId() throws Exception {
        String jsonResources = getResourceFileAsString("json/searchChTestStationWithoutId.json");

        String result = classUnderTest.getIdFromStation(jsonResources);

        Assertions.assertThat(result).isEqualTo("Zürich, Förrlibuckstr. 60/62 ");
    }
}
