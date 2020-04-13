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
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
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
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final HafasCallService hafasCallService = new HafasCallServiceImpl(restTemplateBuilder);

    private final HafasHttpCallBuilderService hafasHttpCallBuilderService = new HafasHttpCallBuilderServiceImpl();

    private final UuidService uuidGenerator = mock(UuidService.class);

    private final HafasMapperService hafasMapperService = new HafasMapperServiceImpl(uuidGenerator);

    private final HafasApiService hafasApiService = new HafasApiServiceImpl(hafasCallService, hafasHttpCallBuilderService, hafasMapperService);

    private final DBApiService classUnderTest = new DBApiServiceImpl(hafasApiService);

    @BeforeEach
    void init() {
        when(uuidGenerator.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(UUID.randomUUID())
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
    }

    @Test
    void test_getJourneysForRouteWith_with_mocked_json_and_apiToken_returns_correct_price_and_result_length() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getDBTokenAndUrl("981067408", "8000105");
        String scheduledResourceJson = getResourceFileAsString("json/dbHafasJourney.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        String travelPointResourceJson = getResourceFileAsString("json/dbHafasKarlsruheTravelPoint.json");
        ResponseEntity<String> travelPointResult = new ResponseEntity<>(travelPointResourceJson, HttpStatus.OK);
        Journey expectedJourney = JourneyObjectMother.getEiderstrasseRendsburgToKarlsruheJourney();
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(travelPointResult)
                .thenReturn(travelPointResult)
                .thenReturn(testResult);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testData);

        Assertions.assertThat(result.size()).isEqualTo(1);
        //noinspection OptionalGetWithoutIsPresent
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getPrice()).isEqualToComparingFieldByField(expectedJourney.getPrice());
    }

}
