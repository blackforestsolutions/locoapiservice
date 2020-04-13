package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelLine;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getRosenhofToHHStadthausbrueckeJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
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
    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_journeys() throws ParseException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        Journey testData = getRosenhofToHHStadthausbrueckeJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getStart()).isEqualToIgnoringGivenFields(testData.getStart());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getDestination()).isEqualToIgnoringGivenFields(testData.getDestination(), "arrivalTime");
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getPrice()).isEqualToIgnoringGivenFields(testData.getPrice());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getChildPrice()).isEqualToIgnoringGivenFields(testData.getChildPrice());

    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_correct_betweenTrips() throws ParseException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        Journey testData = getRosenhofToHHStadthausbrueckeJourney().getBetweenTrips().get(0);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);
        List<Journey> betweenTripsResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips();

        Assertions.assertThat(betweenTripsResult.size()).isEqualTo(1);
        Assertions.assertThat(betweenTripsResult.get(0).getStart()).isEqualToComparingFieldByField(testData.getStart());
        Assertions.assertThat(betweenTripsResult.get(0).getDestination()).isEqualToComparingFieldByField(testData.getDestination());
        Assertions.assertThat(betweenTripsResult.get(0)).isEqualToIgnoringGivenFields(testData, "id", "travelLine", "start", "destination");
    }

    @Test
    void test_getJourneyMapFrom_json_returns_map_with_correct_travelLine() throws ParseException {
        String jsonJourneys = getResourceFileAsString("json/hvvJourney.json");
        TravelLine testData = getRosenhofToHHStadthausbrueckeJourney().getBetweenTrips().get(0).getTravelLine();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneyMapFrom(jsonJourneys);
        TravelLine travelLineResult = result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getTravelLine();

        Assertions.assertThat(travelLineResult.getOrigin()).isEqualToComparingFieldByField(testData.getOrigin());
        Assertions.assertThat(travelLineResult.getDirection()).isEqualToComparingFieldByField(testData.getDirection());
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(0)).isEqualToComparingFieldByField(testData.getBetweenHolds().get(0));
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(1)).isEqualToComparingFieldByField(testData.getBetweenHolds().get(1));
    }

}


