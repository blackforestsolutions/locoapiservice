package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BBCCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.BBCCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperService;
import de.blackforestsolutions.apiservice.service.mapper.BBCMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BBCHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;
import static org.mockito.Mockito.*;


class BBCApiServiceImplTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);
    private final BBCHttpCallBuilderService bbcHttpCallBuilderService = new BBCHttpCallBuilderServiceImpl();
    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);
    private final BBCCallService bbcCallService = new BBCCallServiceImpl(restTemplateBuilder);
    private final UuidService mockedUuidService = Mockito.mock(UuidService.class);
    @InjectMocks
    private final BBCMapperService bbcMapperService = new BBCMapperServiceImpl(mockedUuidService);
    private final BBCApiService classUnderTest = new BBCApiServiceImpl(bbcCallService, bbcHttpCallBuilderService, bbcMapperService);

    @Test
    void test_map_with_trips28_12_2019_ZRH_FRA() {
        ApiTokenAndUrlInformation testToken = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrl();
        String tripsJson = getResourceFileAsString("json/trips28-12-2019-ZRH-FRA.json");
        ResponseEntity<String> journeyTestResult = new ResponseEntity<>(tripsJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(journeyTestResult);
        when(mockedUuidService.createUUID()).thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(testToken);

        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJournesForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws ParseException {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBbcTokenAndUrlIT();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder apiTokenAndUrlInformationBuilder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        apiTokenAndUrlInformationBuilder = apiTokenAndUrlInformationBuilder.buildFrom(apiTokenAndUrlInformation);
        Date dateNow = formatDate(new Date());
        apiTokenAndUrlInformationBuilder.setDepartureDate(dateNow);
        apiTokenAndUrlInformation = apiTokenAndUrlInformationBuilder.build();
        String scheduledResourceJson = getResourceFileAsString("json/trips28-12-2019-ZRH-FRA.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));
        when(mockedUuidService.createUUID()).thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2).thenReturn(TEST_UUID_3);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat("1848602173-zuerich-frankfurt-am-main").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getProviderId());
        Assertions.assertThat("Zürich").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStart().getCity());
        Assertions.assertThat("Frankfurt").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDestination().getCity());
        Assertions.assertThat(21.0).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getPrice().getValue());
        Assertions.assertThat(23.5).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getPriceWithCommision().getValue());
        Assertions.assertThat(407.0).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getDistance().getValue());
        Assertions.assertThat(buildDateFrom("29/12/2019 12:00:00")).isEqualTo(result.get(TEST_UUID_1).getJourney().get().getStartTime());
        Assertions.assertThat("60346540").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getVehicleNumber());
        Assertions.assertThat("BMW530").isEqualTo(result.get(TEST_UUID_1).getJourney().get().getVehicleName());

        Assertions.assertThat("1819468110-zuerich-frankfurt-am-main").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getProviderId());
        Assertions.assertThat("Zürich").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStart().getCity());
        Assertions.assertThat("Frankfurt").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDestination().getCity());
        Assertions.assertThat(17.0).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getPrice().getValue());
        Assertions.assertThat(19.5).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getPriceWithCommision().getValue());
        Assertions.assertThat(413.0).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getDistance().getValue());
        Assertions.assertThat(buildDateFrom("29/12/2019 16:30:00")).isEqualTo(result.get(TEST_UUID_2).getJourney().get().getStartTime());
        Assertions.assertThat("90081710").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getVehicleNumber());
        Assertions.assertThat("VOLKSWAGENPOLO").isEqualTo(result.get(TEST_UUID_2).getJourney().get().getVehicleName());

        Assertions.assertThat("1849864385-zurigo-francoforte-sul-meno").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getProviderId());
        Assertions.assertThat("Zürich").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStart().getCity());
        Assertions.assertThat("Frankfurt").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDestination().getCity());
        Assertions.assertThat(23.0).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getPrice().getValue());
        Assertions.assertThat(26.5).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getPriceWithCommision().getValue());
        Assertions.assertThat(403.0).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getDistance().getValue());
        Assertions.assertThat(buildDateFrom("29/12/2019 17:30:00")).isEqualTo(result.get(TEST_UUID_3).getJourney().get().getStartTime());
        Assertions.assertThat("88772792").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getVehicleNumber());
        Assertions.assertThat("AUDIQ3").isEqualTo(result.get(TEST_UUID_3).getJourney().get().getVehicleName());
    }
}