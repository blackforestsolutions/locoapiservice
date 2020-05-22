package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class SearchChApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = Mockito.mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService = new SearchChHttpCallBuilderServiceImpl();

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    @Mock
    private SearchChMapperService searchChMapperService = Mockito.mock(SearchChMapperService.class);

    @InjectMocks
    private SearchChApiService classUnderTest = new SearchChApiServiceImpl(callService, searchChHttpCallBuilderService, searchChMapperService);

    @Test
    void test_getTravelPointForRouteFromApiWith_mocked_mapper_returns_hashMap() throws IOException {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getSearchChTokenAndUrl();
        String stationJson = getResourceFileAsString("json/searchChTestStation.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(stationJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));
        Map<String, TravelPoint> mockedStationMap = new HashMap<>();
        TravelPoint mockedTravelPoint = TravelPointObjectMother.getEinsiedelnTravelPoint();
        mockedStationMap.put(mockedTravelPoint.getStationId(), mockedTravelPoint);
        when(searchChMapperService.getTravelPointFrom(anyString())).thenReturn(mockedStationMap);

        Map<String, TravelPoint> result = classUnderTest.getTravelPointForRouteFromApiWith(apiTokenAndUrlInformation, "Einsiedeln");

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(mockedTravelPoint.getStationId())).isEqualToComparingFieldByField(mockedTravelPoint);
        Mockito.verify(searchChMapperService, Mockito.times(1)).getTravelPointFrom(stationJson);
    }

}
