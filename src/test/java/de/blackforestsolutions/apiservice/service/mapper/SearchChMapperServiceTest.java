package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.TravelLine;
import de.blackforestsolutions.generatedcontent.searchCh.Route;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojo;
import static org.assertj.core.api.Assertions.assertThat;

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
    void test_getJourneysFrom_with_json_and_return_map_with_journeys() throws JsonProcessingException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_journey() throws ParseException, JsonProcessingException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        Journey testJourneyData = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(testJourneyData, "legs");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_legs() throws ParseException, JsonProcessingException {
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
    void test_getJourneysFrom_with_json_and_return_correct_travelLine() throws ParseException, JsonProcessingException {
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

    @Test
    void test_mapRouteToJourneyMap_with_wrong_pojo_returns_problem_with_nullPointerException() throws JsonProcessingException {
        String json = getResourceFileAsString("json/searchChTestRoute.json");
        Route route = retrieveJsonToPojo(json, Route.class);
        route.getConnections().get(0).getLegs().get(0).setDeparture(null);

        Map<UUID, JourneyStatus> result = ReflectionTestUtils.invokeMethod(classUnderTest, "mapRouteToJourneyMap", route);

        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(Exception.class);
    }
}
