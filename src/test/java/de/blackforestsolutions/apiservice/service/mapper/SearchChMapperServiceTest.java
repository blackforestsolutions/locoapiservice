package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelLine;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;

class SearchChMapperServiceTest {

    @Mock
    private UuidService uuidService = Mockito.mock(UuidService.class);

    @InjectMocks
    private SearchChMapperService classUnderTest = new SearchChMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        Mockito.when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1);
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

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get()).extracting(
                Journey::getStartTime,
                Journey::getArrivalTime,
                Journey::getDuration
        ).contains(
                testJourneyData.getStartTime(),
                testJourneyData.getArrivalTime(),
                testJourneyData.getDuration()
        );
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getStart()).isEqualToIgnoringGivenFields(testJourneyData.getStart(), "platform", "terminal", "city");
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getDestination()).isEqualToIgnoringGivenFields(testJourneyData.getDestination());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_trips_between() throws ParseException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        List<Journey> testBetweenTrips = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey().getBetweenTrips();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips()).extracting(
                Journey::getStartTime,
                Journey::getArrivalTime,
                Journey::getDuration,
                Journey::getUnknownTravelProvider,
                Journey::getVehicleType,
                Journey::getVehicleNumber
        ).contains(
                Tuple.tuple(
                        testBetweenTrips.get(0).getStartTime(),
                        testBetweenTrips.get(0).getArrivalTime(),
                        testBetweenTrips.get(0).getDuration(),
                        testBetweenTrips.get(0).getUnknownTravelProvider(),
                        testBetweenTrips.get(0).getVehicleType(),
                        testBetweenTrips.get(0).getVehicleNumber()
                ),
                Tuple.tuple(
                        testBetweenTrips.get(1).getStartTime(),
                        testBetweenTrips.get(1).getArrivalTime(),
                        testBetweenTrips.get(1).getDuration(),
                        testBetweenTrips.get(1).getUnknownTravelProvider(),
                        testBetweenTrips.get(1).getVehicleType(),
                        testBetweenTrips.get(1).getVehicleNumber()
                ),
                Tuple.tuple(
                        testBetweenTrips.get(2).getStartTime(),
                        testBetweenTrips.get(2).getArrivalTime(),
                        testBetweenTrips.get(2).getDuration(),
                        testBetweenTrips.get(2).getUnknownTravelProvider(),
                        testBetweenTrips.get(2).getVehicleType(),
                        testBetweenTrips.get(2).getVehicleNumber()
                )
        );

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getStart()).isEqualToIgnoringGivenFields(testBetweenTrips.get(0).getStart());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getDestination()).isEqualToIgnoringGivenFields(testBetweenTrips.get(0).getDestination());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(1).getStart()).isEqualToIgnoringGivenFields(testBetweenTrips.get(1).getStart());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(1).getDestination()).isEqualToIgnoringGivenFields(testBetweenTrips.get(1).getDestination());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(2).getStart()).isEqualToIgnoringGivenFields(testBetweenTrips.get(2).getStart());
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(2).getDestination()).isEqualToIgnoringGivenFields(testBetweenTrips.get(2).getDestination());
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysFrom_with_json_and_return_correct_between_holds() throws ParseException {
        String journeyJson = getResourceFileAsString("json/searchChTestRoute.json");
        TravelLine testTravelLine = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey().getBetweenTrips().get(0).getTravelLine();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysFrom(journeyJson);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getTravelLine().getBetweenHolds().size()).isEqualTo(3);

        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getTravelLine().getBetweenHolds().get(0)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(0));
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getTravelLine().getBetweenHolds().get(1)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(1), "country");
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getBetweenTrips().get(0).getTravelLine().getBetweenHolds().get(2)).isEqualToIgnoringGivenFields(testTravelLine.getBetweenHolds().get(2));
    }

    @Test
    void test_getTravelPointsForStationFromApi_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        String jsonResources = getResourceFileAsString("json/searchChTestStation.json");

        Map<String, TravelPoint> result = classUnderTest.getTravelPointFrom(jsonResources);

        Assertions.assertThat("Luzern").isEqualTo(result.get("8505000").getStationName());
        Assertions.assertThat(51.016962568215476).isEqualTo(result.get("8505000").getGpsCoordinates().getLatitude());
        Assertions.assertThat(1.9118631730189604).isEqualTo(result.get("8505000").getGpsCoordinates().getLongitude());

        Assertions.assertThat("Zürich, Förrlibuckstr. 60/62 ").isEqualTo(result.get(TEST_UUID_1.toString()).getStreet());
    }
}
