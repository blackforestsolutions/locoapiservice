package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class HvvMapperServiceTest {

    private final UuidService uuidGenerator = Mockito.mock(UuidService.class);
    @InjectMocks
    private HvvMapperService classUnderTest = new HvvMapperServiceImpl(uuidGenerator);

    @BeforeEach
    void init() {
        Mockito.when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1);
    }

    @Test
    void test_getStationListFrom_json_return_list_with_travelpoints() throws IOException {
        String jsonTravelPoints = getResourceFileAsString("json/hvvStationList.json");
        TravelPoint firstExpectedResult = TravelpointObjectMother.getPinnebergRichardKoehnHvvTravelPoint();
        TravelPoint secondExpectedResult = TravelpointObjectMother.getHvvHauptbahnhofTravelPoint();

        List<TravelPoint> result = classUnderTest.getStationListFrom(jsonTravelPoints);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(firstExpectedResult);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(secondExpectedResult);
        Assertions.assertThat(result).isEqualTo(List.of(firstExpectedResult, secondExpectedResult));
    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_journeys() throws ParseException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        Journey testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).isEqualToComparingFieldByField(testData);
    }

    @Test
    void test_getJourneyMapFrom_json_returns_correct_legs() throws ParseException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        LinkedHashMap<UUID, Leg> testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney().getLegs();

        LinkedHashMap<UUID, Leg> result = classUnderTest.getJourneyMapFrom(jsonJourneys).get(TEST_UUID_1).getJourney().get().getLegs();

        Assertions.assertThat(result.size()).isEqualTo(4);
        Assertions.assertThat(result.get(TEST_UUID_2)).isEqualToComparingFieldByField(testData.get(TEST_UUID_2));
        Assertions.assertThat(result.get(TEST_UUID_3)).isEqualToComparingFieldByField(testData.get(TEST_UUID_3));
        Assertions.assertThat(result.get(TEST_UUID_4)).isEqualToComparingFieldByField(testData.get(TEST_UUID_4));
        Assertions.assertThat(result.get(TEST_UUID_5)).isEqualToComparingFieldByField(testData.get(TEST_UUID_5));
        Assertions.assertThat(result).isEqualToComparingFieldByField(testData);
    }
}


