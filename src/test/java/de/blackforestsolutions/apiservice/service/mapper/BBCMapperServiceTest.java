package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getFlughafenBerlinToHamburgHbfJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BBCMapperServiceTest {

    private final UuidService uuidService = mock(UuidService.class);

    private final BBCMapperService classUnderTest = new BBCMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4);
    }


    @Test
    void test_mapJsonToJourneys_with_mocked_json_and_apiToken_returns() throws JsonProcessingException {
        String json = getResourceFileAsString("json/bbcTest.json");

        Map<UUID, JourneyStatus> result = classUnderTest.mapJsonToJourneys(json);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void test_mapJsonToJourneys_returns_correct_journey() throws ParseException, JsonProcessingException {
        String json = getResourceFileAsString("json/bbcTest.json");
        Journey expectedJourney = getFlughafenBerlinToHamburgHbfJourney();

        Journey result = classUnderTest.mapJsonToJourneys(json).get(TEST_UUID_2).getJourney().get();

        assertThat(result).isEqualToIgnoringGivenFields(expectedJourney, "legs");
        assertThat(result.getLegs().size()).isEqualTo(1);
        assertThat(result.getLegs().get(TEST_UUID_1)).isEqualToIgnoringGivenFields(expectedJourney.getLegs().get(TEST_UUID_1), "price", "travelLine");
        assertThat(result.getLegs().get(TEST_UUID_1).getPrice()).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_1).getPrice());
        assertThat(result.getLegs().get(TEST_UUID_1).getTravelLine()).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_1).getTravelLine());
    }

    @Test
    void test_mapJsonToJourneys_returns_journey_without_vehicleName_and_travelLine() throws ParseException, JsonProcessingException {
        String json = getResourceFileAsString("json/bbcTest.json");
        Journey expectedJourney = getBerlinHbfToHamburgLandwehrJourney();

        Journey result = classUnderTest.mapJsonToJourneys(json).get(TEST_UUID_4).getJourney().get();

        assertThat(result).isEqualToIgnoringGivenFields(expectedJourney, "legs");
        assertThat(result.getLegs().size()).isEqualTo(1);
        assertThat(result.getLegs().get(TEST_UUID_3)).isEqualToIgnoringGivenFields(expectedJourney.getLegs().get(TEST_UUID_3), "price", "travelLine");
        assertThat(result.getLegs().get(TEST_UUID_3).getPrice()).isEqualToComparingFieldByField(expectedJourney.getLegs().get(TEST_UUID_3).getPrice());
        assertThat(result.getLegs().get(TEST_UUID_3).getVehicleName()).isEmpty();
        assertThat(result.getLegs().get(TEST_UUID_3).getTravelLine()).isNull();
    }

    @Test
    void test() throws ParseException, JsonProcessingException {
        String json = getResourceFileAsString("json/bbcFailedTest.json");

        Map<UUID, JourneyStatus> result = classUnderTest.mapJsonToJourneys(json);

        //Assertions.assertThat(result.get(TEST_UUID_2).getProblem().get().getExceptions().get(0), CoreMatchers.instanceOf(NullPointerException.class));
        Assertions.assertThat(result.get(TEST_UUID_2).getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
    }

}
