package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.bahnService.*;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BahnCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BahnCallServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.service.supportservice.UuidServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.bahn.ArrivalBoard;
import de.blackforestsolutions.generatedcontent.bahn.DepartureBoard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;
import static org.mockito.Mockito.*;

class BahnServiceTest {
    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final BahnHttpCallBuilderService bahnHttpCallBuilderService = new BahnHttpCallBuilderSeviceImpl();

    private final BahnCallService bahnCallService = new BahnCallServiceImpl(restTemplateBuilder);

    private final BahnRailwayStationService classUnderTestBahnRailwayStationService = new BahnRailwayStationServiceImpl(bahnCallService, bahnHttpCallBuilderService);

    private final BahnArrivalBoardService classUnderTestBahnArrivalBoardService = new BahnArrivalBoardServiceImpl(bahnCallService, bahnHttpCallBuilderService);

    private final BahnDepartureBoardService classUnderTestBahnDepartureBoardService = new BahnDepartureBoardServiceImpl(bahnCallService, bahnHttpCallBuilderService);

    @Mock
    private UuidService uuidGenerator = mock(UuidServiceImpl.class);

    @InjectMocks
    private BahnJourneyDetailsService classUnderTestBahnJourneyDetailsService = new BahnJourneyDetailsServiceImpl(bahnCallService, bahnHttpCallBuilderService, uuidGenerator);

    @Test
    void test_getTravelPointsForRouteFromApiWith_mocked_rest_service_is_excuted_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnRailwayStationTokenAndUrlIT();
        String bahnRailwayStationRessourcesJson = getResourceFileAsString("json/bahnTestLocation.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(bahnRailwayStationRessourcesJson, HttpStatus.OK);

        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));

        Map<String, TravelPoint> result = classUnderTestBahnRailwayStationService.getTravelPointsForRouteFromApiWith(apiTokenAndUrlInformation);

        Assertions.assertThat("8096003").isEqualTo(result.get("8096003").getStationId());
        Assertions.assertThat("BERLIN").isEqualTo(result.get("8096003").getStationName());
        Assertions.assertThat(13.386988).isEqualTo(result.get("8096003").getGpsCoordinates().getLongitude());
        Assertions.assertThat(52.520501).isEqualTo(result.get("8096003").getGpsCoordinates().getLatitude());
    }

    @Test
    void test_getArrivalBoardForRouteFromApiWith_mocked_rest_service_is_executed_corectly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnArrivalBoardTokenAndUrlIT();
        String bahnArrivalBoardRessourcesJson = getResourceFileAsString("json/bahnTestArrivalBoard.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(bahnArrivalBoardRessourcesJson, HttpStatus.OK);

        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));

        Map<String, ArrivalBoard> result = classUnderTestBahnArrivalBoardService.getArrivalBoardForRouteFromApiWith(apiTokenAndUrlInformation);

        Assertions.assertThat("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getDetailsId());
        Assertions.assertThat("ICE 274").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getName());
        Assertions.assertThat("ICE").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getType());
        Assertions.assertThat("8011160").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getBoardId());
        Assertions.assertThat("8098160").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getStopId());
        Assertions.assertThat("Berlin Hbf &#x0028;tief&#x0029;").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getStopName());
        Assertions.assertThat("2019-07-25T00:04").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getDateTime());
        Assertions.assertThat("Basel SBB").isEqualTo(result.get("20757%2F14691%2F960162%2F473162%2F80%3fstation_evaId%3D8098160").getOrigin());
    }

    @Test
    void test_getDepartureBoardForRouteFromApiWith_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnDepartureBoardTokenAndUrlIT();
        String bahnDepartureBoardRessourceJson = getResourceFileAsString("json/bahnTestDepartureBoard.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(bahnDepartureBoardRessourceJson, HttpStatus.OK);

        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));

        Map<String, DepartureBoard> result = classUnderTestBahnDepartureBoardService.getDepartureBoardForRouteFromApiWith(apiTokenAndUrlInformation);

        Assertions.assertThat("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getDetailsId());
        Assertions.assertThat("ICE 275").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getName());
        Assertions.assertThat("ICE").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getType());
        Assertions.assertThat("8011160").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getBoardId());
        Assertions.assertThat("8011160").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getStopId());
        Assertions.assertThat("Berlin Hbf").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getStopName());
        Assertions.assertThat("2019-07-25T03:57").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getDateTime());
        Assertions.assertThat("13").isEqualTo(result.get("570648%2F205143%2F448324%2F33946%2F80%3fstation_evaId%3D8011160").getTrack());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneyDetailsForRouteFromApiWith_mocked_rest_service_is_excceuted_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrlIT();
        String bahnJourneyDetailsRessourceJson = getResourceFileAsString("json/bahnTestJourneyDetailsID.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(bahnJourneyDetailsRessourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));
        UUID testUUID1 = UUID.randomUUID();
        UUID testUUID2 = UUID.randomUUID();
        UUID testUUID3 = UUID.randomUUID();
        when(uuidGenerator.createUUID())
                .thenReturn(testUUID1)
                .thenReturn(testUUID2)
                .thenReturn(testUUID3);

        Map<UUID, JourneyStatus> result = classUnderTestBahnJourneyDetailsService.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.get(testUUID1).getJourney().get()).extracting(
                Journey::getTravelProvider,
                Journey::getStartTime,
                Journey::getArrivalTime,
                Journey::getDuration,
                Journey::getVehicleName,
                Journey::getVehicleNumber)
                .containsExactly(
                        TravelProvider.DB,
                        generateTimeFromString("10:53"),
                        generateTimeFromString("10:56"),
                        generateDurationFromStartToDestination(generateTimeFromString("10:53"), generateTimeFromString("10:56")),
                        "IC",
                        "IC 386"
                );

        Assertions.assertThat(
                result.get(testUUID1)
                        .getJourney()
                        .get()
                        .getTravelLine()
                        .getBetweenHolds()
                        .values()
                        .size())
                .isEqualTo(0);

        Assertions.assertThat(result.get(testUUID2).getJourney().get()).extracting(
                Journey::getTravelProvider,
                Journey::getStartTime,
                Journey::getArrivalTime,
                Journey::getDuration,
                Journey::getVehicleName,
                Journey::getVehicleNumber)
                .containsExactly(
                        TravelProvider.DB,
                        generateTimeFromString("10:53"),
                        generateTimeFromString("12:11"),
                        generateDurationFromStartToDestination(generateTimeFromString("10:53"), generateTimeFromString("12:11")),
                        "IC",
                        "IC 386"
                );

        Assertions.assertThat(result.get(testUUID3).getJourney().get()).extracting(
                Journey::getTravelProvider,
                Journey::getStartTime,
                Journey::getArrivalTime,
                Journey::getDuration,
                Journey::getVehicleName,
                Journey::getVehicleNumber)
                .containsExactly(
                        TravelProvider.DB,
                        generateTimeFromString("10:58"),
                        generateTimeFromString("12:11"),
                        generateDurationFromStartToDestination(generateTimeFromString("10:58"), generateTimeFromString("12:11")),
                        "IC",
                        "IC 386"
                );

        Assertions.assertThat(result.get(testUUID3).getJourney().get().getTravelLine().getBetweenHolds().values().size()).isEqualTo(0);

        Assertions.assertThat(result.get(testUUID1).getJourney().get().getStart()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8002549",
                        "Hamburg Hbf",
                        generateCoordinatesFrom("53.552733", "10.006909")
                );

        Assertions.assertThat(result.get(testUUID1).getJourney().get().getDestination()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8002548",
                        "Hamburg Dammtor",
                        generateCoordinatesFrom("53.560751", "9.989569")
                );

        Assertions.assertThat(result.get(testUUID2).getJourney().get().getStart()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8002549",
                        "Hamburg Hbf",
                        generateCoordinatesFrom("53.552733", "10.006909")
                );

        Assertions.assertThat(result.get(testUUID2).getJourney().get().getDestination()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8000312",
                        "Rendsburg",
                        generateCoordinatesFrom("54.302262", "9.671135")
                );

        Assertions.assertThat(result.get(testUUID3).getJourney().get().getStart()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8002548",
                        "Hamburg Dammtor",
                        generateCoordinatesFrom("53.560751", "9.989569")
                );

        Assertions.assertThat(result.get(testUUID3).getJourney().get().getDestination()).extracting(
                TravelPoint::getStationId,
                TravelPoint::getStationName,
                TravelPoint::getGpsCoordinates)
                .containsExactly(
                        "8000312",
                        "Rendsburg",
                        generateCoordinatesFrom("54.302262", "9.671135")
                );
    }

    private Coordinates generateCoordinatesFrom(String latitude, String longitude) {
        return new Coordinates.CoordinatesBuilder(Double.parseDouble(latitude), Double.parseDouble(longitude)).build();
    }
}