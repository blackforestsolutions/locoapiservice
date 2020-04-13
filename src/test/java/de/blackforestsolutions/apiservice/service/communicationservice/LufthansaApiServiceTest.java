package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.LufthansaCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.LufthansaCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperService;
import de.blackforestsolutions.apiservice.service.mapper.LufthansaMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.LuftHansaHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getLufthansaTokenAndUrlIT;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.formatDate;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LufthansaApiServiceTest {

    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final LufthansaCallService lufthansaCallService = new LufthansaCallServiceImpl(restTemplateBuilder);

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final UuidService uuidService = mock(UuidService.class);

    private final LufthansaMapperService mapper = new LufthansaMapperServiceImpl(airportConfiguration.airports(), uuidService);

    private final LuftHansaHttpCallBuilderService httpCallBuilderService = new LuftHansaHttpCallBuilderServiceImpl();

    private final LufthansaApiService classUnderTest = new LufthansaApiServiceImpl(lufthansaCallService, httpCallBuilderService, mapper);

    LufthansaApiServiceTest() throws IOException {
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void test_getJourneysForRouteFromApiWith_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getLufthansaTokenAndUrlIT();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        Date now = formatDate(new Date());
        builder.setArrivalDate(now);
        builder.setDepartureDate(now);
        apiTokenAndUrlInformation = builder.build();
        String scheduledResourcesJson = getResourceFileAsString("json/lufthansatest.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(scheduledResourcesJson, HttpStatus.OK);
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1).thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3).thenReturn(TEST_UUID_4)
                .thenReturn(TEST_UUID_5).thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7).thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9).thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11).thenReturn(TEST_UUID_12);

        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), any(), any(Class.class));

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        assertThat("E90").isEqualTo(result.get(TEST_UUID_6).getJourney().get().getVehicleNumber());
        assertThat(TravelProvider.LUFTHANSA).isEqualTo(result.get(TEST_UUID_6).getJourney().get().getTravelProvider());
        assertThat("LH1191").isEqualTo(result.get(TEST_UUID_6).getJourney().get().getProviderId());
    }
}
