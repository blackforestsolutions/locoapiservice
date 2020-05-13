package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.LegObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
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

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getNahShPrice;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NahShApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final HafasHttpCallBuilderService hafasHttpCallBuilderService = new HafasHttpCallBuilderServiceImpl();

    private final UuidService uuidGenerator = mock(UuidService.class);

    private final HafasMapperService hafasMapperService = new HafasMapperServiceImpl(uuidGenerator);

    private final HafasApiService hafasApiService = new HafasApiServiceImpl(callService, hafasHttpCallBuilderService, hafasMapperService);

    private final NahShApiService classUnderTest = new NahShApiServiceImpl(hafasApiService);

    @BeforeEach
    void init() {
        String scheduledResourceJson = getResourceFileAsString("json/nahShHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        String travelPointResourceJson = getResourceFileAsString("json/dbHafasRendsburgTravelPoint.json");
        ResponseEntity<String> travelPointResult = new ResponseEntity<>(travelPointResourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(travelPointResult)
                .thenReturn(travelPointResult)
                .thenReturn(testResult);
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_price() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Leg expectedJourney = LegObjectMother.getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getNahShPrice());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2);

        Assertions.assertThat(legResult.getPrice()).isEqualToComparingFieldByField(expectedJourney.getPrice());
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_journey() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToRendsburgJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(journeyResult).isEqualToIgnoringGivenFields(expectedJourney, "legs");
        Assertions.assertThat(journeyResult.getLegs().size()).isEqualTo(3);
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_eiderstrasse_and_gartenstrasse() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Leg expectedLeg = LegObjectMother.getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getNahShPrice());

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2);

        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "price");
        Assertions.assertThat(legResult.getPrice()).isEqualToComparingFieldByField(expectedLeg.getPrice());
        Assertions.assertThat(legResult.getTravelLine()).isNull();
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_gartenstrasse_and_rendsburg_zob() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Leg expectedLeg = LegObjectMother.getGartenstrasseRendsburgLeg(TravelProvider.NAHSH);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_3);

        Assertions.assertThat(legResult).isEqualToIgnoringGivenFields(expectedLeg, "travelLine");
        Assertions.assertThat(legResult.getTravelLine()).isEqualToIgnoringGivenFields(expectedLeg.getTravelLine(), "betweenHolds");
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().size()).isEqualTo(5);
        Assertions.assertThat(legResult.getTravelLine().getBetweenHolds().get(0)).isEqualToComparingFieldByField(expectedLeg.getTravelLine().getBetweenHolds().get(0));
    }

    @Test
    void test_getJourneysFrom_with_jsonBody_travelProvider_and_mocked_priceMapper_returns_correct_leg_between_rendsburg_zob_and_rendsburg() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getNahShTokenAndUrl("", "");
        Leg expectedLeg = LegObjectMother.getRendsburgZobToRendsburgLeg();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);
        //noinspection OptionalGetWithoutIsPresent
        Leg legResult = result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_4);

        Assertions.assertThat(legResult).isEqualToComparingFieldByField(expectedLeg);
        Assertions.assertThat(legResult.getTravelLine()).isNull();
    }
}
