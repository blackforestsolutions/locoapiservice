package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.hvv.response.journey.HvvRoute;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojo;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class HvvMapperServiceTest {

    private final UuidService uuidGenerator = Mockito.mock(UuidService.class);
    @InjectMocks
    private HvvMapperService classUnderTest = new HvvMapperServiceImpl(uuidGenerator);

    @BeforeEach
    void init() {
        Mockito.when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
    }

    @Test
    void test_getStationListFrom_json_return_list_with_travelpoints() throws IOException {
        String jsonTravelPoints = getResourceFileAsString("json/hvvStationList.json");
        TravelPoint firstExpectedResult = TravelPointObjectMother.getPinnebergRichardKoehnHvvTravelPoint();
        TravelPoint secondExpectedResult = TravelPointObjectMother.getHvvHauptbahnhofTravelPoint();

        List<TravelPoint> result = classUnderTest.getStationListFrom(jsonTravelPoints);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(firstExpectedResult);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(secondExpectedResult);
        Assertions.assertThat(result).isEqualTo(List.of(firstExpectedResult, secondExpectedResult));
    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_journeys() throws ParseException, JsonProcessingException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        Journey testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(testData, "legs");
    }

    @Test
    void test_getJourneyMapFrom_json_returns_correct_legs() throws ParseException, JsonProcessingException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        LinkedHashMap<UUID, Leg> testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney().getLegs();

        LinkedHashMap<UUID, Leg> result = classUnderTest.getJourneyMapFrom(jsonJourneys).get(TEST_UUID_1).getJourney().get().getLegs();

        Assertions.assertThat(result.size()).isEqualTo(4);
        Assertions.assertThat(result.get(TEST_UUID_2)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_2), "price");
        Assertions.assertThat(result.get(TEST_UUID_2).getPrice()).isEqualToComparingFieldByField(testData.get(TEST_UUID_2).getPrice());
        Assertions.assertThat(result.get(TEST_UUID_3)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_3), "travelLine");
        Assertions.assertThat(result.get(TEST_UUID_3).getTravelLine()).isEqualToComparingFieldByField(testData.get(TEST_UUID_3).getTravelLine());
        Assertions.assertThat(result.get(TEST_UUID_3).getPrice()).isNull();
        Assertions.assertThat(result.get(TEST_UUID_4)).isEqualToComparingFieldByField(testData.get(TEST_UUID_4));
        Assertions.assertThat(result.get(TEST_UUID_4).getPrice()).isNull();
        Assertions.assertThat(result.get(TEST_UUID_5)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_5), "travelLine");
        Assertions.assertThat(result.get(TEST_UUID_5).getTravelLine()).isEqualToComparingFieldByField(testData.get(TEST_UUID_5).getTravelLine());
        Assertions.assertThat(result.get(TEST_UUID_5).getPrice()).isNull();
    }

    @Test
    void test_mapHvvRouteToJourneyMap_with_wrong_pojo_returns_problem_with_nullPointerException() throws JsonProcessingException {
        String json = getResourceFileAsString("json/hvvJourney.json");
        HvvRoute hvvRoute = retrieveJsonToPojo(json, HvvRoute.class);
        hvvRoute.getRealtimeSchedules().get(0).getScheduleElements().get(0).setFrom(null);

        Map<UUID, JourneyStatus> result = ReflectionTestUtils.invokeMethod(classUnderTest, "mapHvvRouteToJourneyMap", hvvRoute);

        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(Exception.class);
    }
}


