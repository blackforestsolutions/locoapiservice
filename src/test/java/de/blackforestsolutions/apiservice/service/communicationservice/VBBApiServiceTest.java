package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HafasCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperService;
import de.blackforestsolutions.apiservice.service.mapper.HafasMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VBBApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final HafasCallService hafasCallService = new HafasCallServiceImpl(restTemplateBuilder);

    private final HafasHttpCallBuilderService hafasHttpCallBuilderService = new HafasHttpCallBuilderServiceImpl();

    private final UuidService uuidGenerator = mock(UuidService.class);

    private final HafasMapperService hafasMapperService = new HafasMapperServiceImpl(uuidGenerator);

    private final HafasApiService hafasApiService = new HafasApiServiceImpl(hafasCallService, hafasHttpCallBuilderService, hafasMapperService);

    private final VBBApiService classUnderTest = new VBBApiServiceImpl(hafasApiService);

    @BeforeEach
    void init() {
        String scheduledResourceJson = getResourceFileAsString("json/vbbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        String travelPointResourceJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> travelPointResult = new ResponseEntity<>(travelPointResourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(travelPointResult)
                .thenReturn(travelPointResult)
                .thenReturn(testResult);
        when(uuidGenerator.createUUID()).thenReturn(TEST_UUID_1);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_journey() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        Journey expectedJourney = JourneyObjectMother.getPotsdamBerlinJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(journeyResult.getStartTime()).isEqualTo(expectedJourney.getStartTime());
        Assertions.assertThat(journeyResult.getArrivalTime()).isEqualTo(expectedJourney.getArrivalTime());
        Assertions.assertThat(journeyResult.getDuration()).isEqualTo(expectedJourney.getDuration());
        Assertions.assertThat(journeyResult.getTravelProvider()).isEqualTo(expectedJourney.getTravelProvider());
        Assertions.assertThat(journeyResult.getProviderId()).isEqualTo(expectedJourney.getProviderId());
        Assertions.assertThat(journeyResult.getVehicleType()).isEqualTo(expectedJourney.getVehicleType());
        Assertions.assertThat(journeyResult.getVehicleName()).isEqualTo(expectedJourney.getVehicleName());
        Assertions.assertThat(journeyResult.getVehicleNumber()).isEqualTo(expectedJourney.getVehicleNumber());
        Assertions.assertThat(journeyResult)
                .isEqualToIgnoringGivenFields(expectedJourney, "start", "destination", "price", "travelLine");
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_start_and_destination() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        Journey expectedJourney = JourneyObjectMother.getPotsdamBerlinJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(journeyResult.getStart()).isEqualToComparingFieldByField(expectedJourney.getStart());
        Assertions.assertThat(journeyResult.getDestination()).isEqualToComparingFieldByField(expectedJourney.getDestination());
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_price() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        Price expectedPrice = JourneyObjectMother.getPotsdamBerlinJourney().getPrice();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(journeyResult.getPrice()).isEqualToComparingFieldByField(expectedPrice);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_travelLine() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        TravelLine expectedTravelLine = JourneyObjectMother.getPotsdamBerlinJourney().getTravelLine();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        TravelLine travelLineResult = result.get(TEST_UUID_1).getJourney().get().getTravelLine();

        Assertions.assertThat(travelLineResult.getDirection()).isEqualToComparingFieldByField(expectedTravelLine.getDirection());
        Assertions.assertThat(travelLineResult.getBetweenHolds().size()).isEqualToComparingFieldByField(expectedTravelLine.getBetweenHolds().size());
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedTravelLine.getBetweenHolds().get(0));
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(1)).isEqualToComparingFieldByField(expectedTravelLine.getBetweenHolds().get(1));
        Assertions.assertThat(travelLineResult.getBetweenHolds().get(2)).isEqualToComparingFieldByField(expectedTravelLine.getBetweenHolds().get(2));
    }

}
