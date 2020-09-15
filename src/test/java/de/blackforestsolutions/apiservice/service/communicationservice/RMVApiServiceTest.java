package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperService;
import de.blackforestsolutions.apiservice.service.mapper.RMVMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.*;
import de.blackforestsolutions.datamodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.springframework.data.geo.Box;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.UnmarshalException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.BoxObjectMother.getRmvBox;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RMVApiServiceTest {

    private final CallService callService = mock(CallService.class);
    private final RMVHttpCallBuilderService httpCallBuilderService = spy(new RMVHttpCallBuilderServiceImpl());
    private final UuidService uuidService = new UuidServiceImpl();
    private final RMVMapperService rmvMapperService = spy(new RMVMapperServiceImpl(uuidService));
    private final BoxService boxService = mock(BoxService.class);
    private final Box rmvBox = getRmvBox();

    @InjectMocks
    private final RMVApiService classUnderTest = new RMVApiServiceImpl(callService, httpCallBuilderService, rmvMapperService, boxService, rmvBox);

    @BeforeEach
    void init() {
        String departureCall = getResourceFileAsString("xml/LocationList.xml");
        String arrivalCall = getResourceFileAsString("xml/LocationList-frankfurt.xml");
        String tripListXml = getResourceFileAsString("xml/TripList.xml");
        when(callService.getOld(anyString(), any(HttpEntity.class)))
                .thenReturn(new ResponseEntity<>(departureCall, HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(arrivalCall, HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(tripListXml, HttpStatus.OK));
        when(boxService.isCoordinateInBox(any(Coordinates.class), any(Box.class)))
                .thenReturn(true);
    }


    @Test
    void test_getJourneysForRouteBySearchStringWith_mocked_call_service_is_executed_correctly_and_map_correctly() throws Exception {
        String expectedDeparture = "Lorch-Lorchhausen Bahnhof";
        String expectedArrival = "frankfurt hauptbahnhof";
        ApiTokenAndUrlInformation testToken = getRMVTokenAndUrl(expectedDeparture, expectedArrival);
        ArgumentCaptor<String> httpCallBuilderArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> callServiceArg = ArgumentCaptor.forClass(String.class);


        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteBySearchStringWith(testToken).getCalledObject();

        InOrder inOrder = inOrder(httpCallBuilderService, callService, rmvMapperService);
        inOrder.verify(httpCallBuilderService, times(2)).buildLocationStringPathWith(any(ApiTokenAndUrlInformation.class), httpCallBuilderArg.capture());
        inOrder.verify(callService, times(1)).getOld(anyString(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getIdFrom(anyString());
        inOrder.verify(callService, times(1)).getOld(anyString(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getIdFrom(anyString());
        inOrder.verify(httpCallBuilderService, times(1)).buildTripPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(callService, times(1)).getOld(callServiceArg.capture(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getJourneysFrom(anyString());
        assertThat(result.size()).isEqualTo(6);
        assertThat(httpCallBuilderArg.getAllValues()).isEqualTo(List.of(expectedDeparture, expectedArrival));
        assertThat(callServiceArg.getValue()).contains("originId=A%3D2%40O%3DLorch+-+Lorchhausen%2C+Oberflecken%40X%3D7785108%40Y%3D50053277%40U%3D103%40b%3D990117421%40B%3D1%40V%3D6.9%2C%40p%3D1530862110%40");
        assertThat(callServiceArg.getValue()).contains("destId=A=1@O=Frankfurt (Main) Hauptbahnhof@X=8662653@Y=50106808@U=80@L=003000010@B=1@V=6.9,@p=1575313337@");
    }

    @Test
    void test_getJourneysForRouteByCoordinatesWith_mocked_rest_service_is_executed_correctly_and_map_correctly() throws Exception {
        ApiTokenAndUrlInformation testToken = getRMVTokenAndUrl("", "");
        ArgumentCaptor<Coordinates> httpCallBuilderArg = ArgumentCaptor.forClass(Coordinates.class);
        ArgumentCaptor<String> callServiceArg = ArgumentCaptor.forClass(String.class);


        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteByCoordinatesWith(testToken).getCalledObject();

        InOrder inOrder = inOrder(httpCallBuilderService, callService, rmvMapperService);
        inOrder.verify(httpCallBuilderService, times(2)).buildLocationCoordinatesPathWith(any(ApiTokenAndUrlInformation.class), httpCallBuilderArg.capture());
        inOrder.verify(callService, times(1)).getOld(anyString(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getIdFrom(anyString());
        inOrder.verify(callService, times(1)).getOld(anyString(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getIdFrom(anyString());
        inOrder.verify(httpCallBuilderService, times(1)).buildTripPathWith(any(ApiTokenAndUrlInformation.class));
        inOrder.verify(callService, times(1)).getOld(callServiceArg.capture(), any(HttpEntity.class));
        inOrder.verify(rmvMapperService, times(1)).getJourneysFrom(anyString());
        assertThat(result.size()).isEqualTo(6);
        assertThat(httpCallBuilderArg.getAllValues()).isEqualTo(List.of(
                getRMVTokenAndUrl("", "").getDepartureCoordinates(),
                getRMVTokenAndUrl("", "").getArrivalCoordinates())
        );
        assertThat(callServiceArg.getValue()).contains("originId=A%3D2%40O%3DLorch+-+Lorchhausen%2C+Oberflecken%40X%3D7785108%40Y%3D50053277%40U%3D103%40b%3D990117421%40B%3D1%40V%3D6.9%2C%40p%3D1530862110%40");
        assertThat(callServiceArg.getValue()).contains("destId=A=1@O=Frankfurt (Main) Hauptbahnhof@X=8662653@Y=50106808@U=80@L=003000010@B=1@V=6.9,@p=1575313337@");
    }

    @Test
    void test_getJourneysForRouteByCoordinatesWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getRMVTokenAndUrl("", ""));
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinatesWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteByCoordinatesWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");
        when(callService.getOld(anyString(), any())).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinatesWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(UnmarshalException.class);
    }

    @Test
    void test_getJourneysForRouteByCoordinatesWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");
        doThrow(new RuntimeException()).when(callService).getOld(anyString(), any());

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinatesWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_getJourneysForRouteBySearchStringWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getRMVTokenAndUrl("", ""));
        testData.setHost(null);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteBySearchStringWith(testData.build());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getJourneysForRouteBySearchStringWith_apiToken_and_wrong_mocked_http_answer_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");
        when(callService.getOld(anyString(), any())).thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteBySearchStringWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(UnmarshalException.class);
    }

    @Test
    void test_getJourneysForRouteBySearchStringWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");
        doThrow(new RuntimeException()).when(callService).getOld(anyString(), any());

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteBySearchStringWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_getJourneysForRouteByCoordinatesWith_apiToken_and_no_coordinates_in_box_returns_emptyMap() {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");
        when(boxService.isCoordinateInBox(any(Coordinates.class), any(Box.class)))
                .thenReturn(false);

        CallStatus<Map<UUID, JourneyStatus>> result = classUnderTest.getJourneysForRouteByCoordinatesWith(testData);

        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getCalledObject()).isEqualTo(Collections.emptyMap());
    }
}
