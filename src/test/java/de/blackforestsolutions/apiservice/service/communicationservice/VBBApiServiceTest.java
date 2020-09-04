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
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Leg;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_2;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VBBApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder, WebClient.create());

    private final HafasHttpCallBuilderService hafasHttpCallBuilderService = new HafasHttpCallBuilderServiceImpl();

    private final UuidService uuidGenerator = mock(UuidService.class);

    private final HafasMapperService hafasMapperService = new HafasMapperServiceImpl(uuidGenerator);

    private final HafasApiService hafasApiService = new HafasApiServiceImpl(callService, hafasHttpCallBuilderService, hafasMapperService);

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
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_journey() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        Journey expectedJourney = JourneyObjectMother.getPotsdamBerlinJourney();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData).getCalledObject();
        //noinspection OptionalGetWithoutIsPresent
        Journey journeyResult = result.get(TEST_UUID_1).getJourney().get();

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(journeyResult).isEqualToIgnoringGivenFields(expectedJourney, "legs");
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_leg() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getVBBTokenAndUrl("900230999", "900140006");
        Leg expectedLeg = LegObjectMother.getPotsdamBerlinLeg();

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData).getCalledObject();
        //noinspection OptionalGetWithoutIsPresent
        LinkedHashMap<UUID, Leg> legsResult = result.get(TEST_UUID_1).getJourney().get().getLegs();

        Assertions.assertThat(legsResult.size()).isEqualTo(1);
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getStartTime()).isEqualTo(expectedLeg.getStartTime());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getArrivalTime()).isEqualTo(expectedLeg.getArrivalTime());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getDuration()).isEqualTo(expectedLeg.getDuration());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getTravelProvider()).isEqualTo(expectedLeg.getTravelProvider());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getProviderId()).isEqualTo(expectedLeg.getProviderId());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getVehicleType()).isEqualTo(expectedLeg.getVehicleType());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getVehicleName()).isEqualTo(expectedLeg.getVehicleName());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getVehicleNumber()).isEqualTo(expectedLeg.getVehicleNumber());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getStart()).isEqualToComparingFieldByField(expectedLeg.getStart());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getDestination()).isEqualToComparingFieldByField(expectedLeg.getDestination());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getPrice()).isEqualToComparingFieldByField(expectedLeg.getPrice());
        Assertions.assertThat(legsResult.get(TEST_UUID_2).getTravelLine()).isEqualToComparingFieldByField(expectedLeg.getTravelLine());
        Assertions.assertThat(legsResult.get(TEST_UUID_2)).isEqualToIgnoringGivenFields(expectedLeg, "price", "travelLine");
    }
}
