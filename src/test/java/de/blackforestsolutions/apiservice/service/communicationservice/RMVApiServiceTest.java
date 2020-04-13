package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.RMVCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.RMVCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperService;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.RMVHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.formatDate;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RMVApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final RMVCallService rmvCallService = new RMVCallServiceImpl(restTemplateBuilder);

    private final RMVHttpCallBuilderService httpCallBuilderService = new RMVHttpCallBuilderServiceImpl();

    private final UuidService uuidService = mock(UuidService.class);

    @InjectMocks
    private final RMVMapperService rmvMapperService = new RMVMapperServiceImpl(uuidService);

    private final RMVApiService classUnderTest = new RMVApiServiceImpl(rmvCallService, httpCallBuilderService, rmvMapperService);

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        Date now = formatDate(new Date());
        builder.setArrivalDate(now);
        builder.setDepartureDate(now);
        builder.setLocationPath("hapi/location.name?");
        apiTokenAndUrlInformation = builder.build();
        String departureCall = getResourceFileAsString("xml/LocationList.xml");
        ResponseEntity<String> departureCallResult = new ResponseEntity<>(departureCall, HttpStatus.OK);
        String arrivalCall = getResourceFileAsString("xml/LocationList-frankfurt.xml");
        ResponseEntity<String> arrivalCallResult = new ResponseEntity<>(arrivalCall, HttpStatus.OK);
        String tripListXml = getResourceFileAsString("xml/TripList.xml");
        ResponseEntity<String> testResult = new ResponseEntity<>(tripListXml, HttpStatus.OK);
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(departureCallResult).thenReturn(arrivalCallResult).thenReturn(testResult);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.size()).isEqualTo(5);
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        Assertions.assertThat(result.get(TEST_UUID_2).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        Assertions.assertThat(result.get(TEST_UUID_2).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        Assertions.assertThat(result.get(TEST_UUID_2).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        Assertions.assertThat(result.get(TEST_UUID_2).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        Assertions.assertThat(result.get(TEST_UUID_3).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        Assertions.assertThat(result.get(TEST_UUID_3).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        Assertions.assertThat(result.get(TEST_UUID_3).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        Assertions.assertThat(result.get(TEST_UUID_3).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        Assertions.assertThat(result.get(TEST_UUID_4).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        Assertions.assertThat(result.get(TEST_UUID_4).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        Assertions.assertThat(result.get(TEST_UUID_4).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        Assertions.assertThat(result.get(TEST_UUID_4).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
        Assertions.assertThat(result.get(TEST_UUID_5).getJourney().get().getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof tief");
        Assertions.assertThat(result.get(TEST_UUID_5).getJourney().get().getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        Assertions.assertThat(result.get(TEST_UUID_5).getJourney().get().getPrice().getValue()).isEqualTo(1235.0);
        Assertions.assertThat(result.get(TEST_UUID_1).getJourney().get().getTravelLine().getBetweenHolds().size()).isEqualTo(2);
    }
}
