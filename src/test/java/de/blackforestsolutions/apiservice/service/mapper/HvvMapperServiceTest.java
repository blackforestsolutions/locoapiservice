package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.exception.NoExternalResultFoundException;
import de.blackforestsolutions.generatedcontent.hvv.response.journey.HvvRoute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.retrieveJsonToPojo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HvvMapperServiceTest {

    private final UuidService uuidGenerator = mock(UuidService.class);

    @InjectMocks
    private HvvMapperService classUnderTest = new HvvMapperServiceImpl(uuidGenerator);

    @BeforeEach
    void init() {
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_journeys() throws JsonProcessingException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        Journey testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToIgnoringGivenFields(testData, "legs");
    }

    @Test
    void test_getJourneyMapFrom_json_returns_correct_legs() throws JsonProcessingException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        LinkedHashMap<UUID, Leg> testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney().getLegs();

        LinkedHashMap<UUID, Leg> result = classUnderTest.getJourneyMapFrom(jsonJourneys).get(TEST_UUID_1).getJourney().get().getLegs();

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(TEST_UUID_2)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_2), "price");
        assertThat(result.get(TEST_UUID_2).getPrice()).isEqualToComparingFieldByField(testData.get(TEST_UUID_2).getPrice());
        assertThat(result.get(TEST_UUID_3)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_3), "travelLine");
        assertThat(result.get(TEST_UUID_3).getTravelLine()).isEqualToComparingFieldByField(testData.get(TEST_UUID_3).getTravelLine());
        assertThat(result.get(TEST_UUID_3).getPrice()).isNull();
        assertThat(result.get(TEST_UUID_4)).isEqualToComparingFieldByField(testData.get(TEST_UUID_4));
        assertThat(result.get(TEST_UUID_4).getPrice()).isNull();
        assertThat(result.get(TEST_UUID_5)).isEqualToIgnoringGivenFields(testData.get(TEST_UUID_5), "travelLine");
        assertThat(result.get(TEST_UUID_5).getTravelLine()).isEqualToComparingFieldByField(testData.get(TEST_UUID_5).getTravelLine());
        assertThat(result.get(TEST_UUID_5).getPrice()).isNull();
    }

    @Test
    void test_mapHvvRouteToJourneyMap_with_wrong_pojo_returns_problem_with_nullPointerException() {
        String json = getResourceFileAsString("json/hvvJourney.json");
        HvvRoute hvvRoute = retrieveJsonToPojo(json, HvvRoute.class);
        hvvRoute.getRealtimeSchedules().get(0).getScheduleElements().get(0).setFrom(null);

        Map<UUID, JourneyStatus> result = ReflectionTestUtils.invokeMethod(classUnderTest, "mapHvvRouteToJourneyMap", hvvRoute);

        //noinspection ConstantConditions
        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
        assertThat(result.get(TEST_UUID_3).getProblem().get().getExceptions().get(0)).isInstanceOf(Exception.class);
    }

    @Test
    void test_getHvvStationFrom_with_wrong_json_throws_NoExternalResultFoundException() {
        String noJourneyFoundJson = "{\"returnCode\":\"OK\"}";

        Assertions.assertThrows(
                NoExternalResultFoundException.class,
                () -> classUnderTest.getHvvStationFrom(noJourneyFoundJson)
        );
    }
}


