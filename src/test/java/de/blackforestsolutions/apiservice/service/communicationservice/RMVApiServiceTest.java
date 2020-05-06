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
import de.blackforestsolutions.datamodel.PriceCategory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.formatDate;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
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
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl("Lorch-Lorchhausen Bahnhof", "frankfurt hauptbahnhof");
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
                .thenReturn(TEST_UUID_5)
                .thenReturn(TEST_UUID_6)
                .thenReturn(TEST_UUID_7)
                .thenReturn(TEST_UUID_8)
                .thenReturn(TEST_UUID_9)
                .thenReturn(TEST_UUID_10)
                .thenReturn(TEST_UUID_11).thenReturn(TEST_UUID_12).thenReturn(TEST_UUID_13).thenReturn(TEST_UUID_14).thenReturn(TEST_UUID_15).thenReturn(TEST_UUID_16).thenReturn(TEST_UUID_17).thenReturn(TEST_UUID_18).thenReturn(TEST_UUID_19)
                .thenReturn(TEST_UUID_20).thenReturn(TEST_UUID_21).thenReturn(TEST_UUID_22);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(departureCallResult).thenReturn(arrivalCallResult).thenReturn(testResult);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_1).getJourney().get().getLegs().get(TEST_UUID_2).isHasPrice()).isEqualTo(true);

        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_3).getJourney().get().getLegs().get(TEST_UUID_4).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_5).getJourney().get().getLegs().get(TEST_UUID_6).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_7).getJourney().get().getLegs().get(TEST_UUID_8).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));

        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getStart().getStationName()).isEqualTo("Frankfurt (Main) Hauptbahnhof tief");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getDestination().getStationName()).isEqualTo("Wiesbaden Hauptbahnhof");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getPrice().getValues().get(PriceCategory.ADULT)).isEqualTo(new BigDecimal(1235));
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_10).getPrice().getValues().get(PriceCategory.CHILD)).isEqualTo(new BigDecimal(730));
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_11).getStart().getStationName()).isEqualTo("Wiesbaden Hauptbahnhof");
        assertThat(result.get(TEST_UUID_9).getJourney().get().getLegs().get(TEST_UUID_11).getDestination().getStationName()).isEqualTo("Lorch-Lorchhausen Bahnhof");
    }
}
