package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.mapper.OsmMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStuttgartWaiblingerStreetTravelPoint;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OSMApiServiceTest {

    private final CallService callService = mock(CallService.class);

    private final OSMHttpCallBuilderService osmHttpCallBuilderService = mock(OSMHttpCallBuilderService.class);

    private final OsmMapperService osmMapperService = mock(OsmMapperService.class);

    private final OSMApiService classUnderTest = new OSMApiServiceImpl(callService, osmHttpCallBuilderService, osmMapperService);

    @BeforeEach
    void init() {
        String addressJson = getResourceFileAsString("json/osmTravelPointAddress.json");

        when(callService.getOld(anyString(), any(HttpEntity.class))).thenReturn(new ResponseEntity<>(addressJson, HttpStatus.OK));

        when(osmHttpCallBuilderService.buildOSMPathWith(any(ApiTokenAndUrlInformation.class), anyString())).thenReturn("");

        when(osmMapperService.mapOsmJsonToTravelPoint(anyString())).thenReturn(
                new TravelPointStatus(Optional.of(getStuttgartWaiblingerStreetTravelPoint()), Optional.empty())
        );
    }

    @Test
    void test_getTravelPointFrom_with_mocked_services_is_executed_correctly() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getOSMApiTokenAndUrl());
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

        CallStatus<TravelPointStatus> result = classUnderTest.getTravelPointFrom(testData.build(), testData.getArrival());

        InOrder inOrder = inOrder(callService, osmHttpCallBuilderService, osmMapperService);
        inOrder.verify(osmHttpCallBuilderService, times(1)).buildOSMPathWith(any(ApiTokenAndUrlInformation.class), anyString());
        inOrder.verify(callService, times(1)).getOld(url.capture(), httpEntity.capture());
        inOrder.verify(osmMapperService, times(1)).mapOsmJsonToTravelPoint(body.capture());
        assertThat(url.getValue()).isEqualTo("https://nominatim.openstreetmap.org");
        assertThat(body.getValue()).isEqualTo(getResourceFileAsString("json/osmTravelPointAddress.json"));
        assertThat(httpEntity.getValue()).isEqualToComparingFieldByField(HttpEntity.EMPTY);
        assertThat(result.getCalledObject().getTravelPoint().get()).isEqualToComparingFieldByField(getStuttgartWaiblingerStreetTravelPoint());
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getOSMApiTokenAndUrl());
        testData.setHost(null);

        CallStatus<TravelPointStatus> result = classUnderTest.getTravelPointFrom(testData.build(), testData.getArrival());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_apiToken_throws_exception_during_http_call_returns_failed_call_status() {
        ApiTokenAndUrlInformation testData = getOSMApiTokenAndUrl();
        doThrow(new RuntimeException()).when(callService).getOld(anyString(), any(HttpEntity.class));

        CallStatus<TravelPointStatus> result = classUnderTest.getTravelPointFrom(testData, testData.getArrival());

        assertThat(result.getStatus()).isEqualTo(Status.FAILED);
        assertThat(result.getThrowable()).isInstanceOf(RuntimeException.class);
    }
}
