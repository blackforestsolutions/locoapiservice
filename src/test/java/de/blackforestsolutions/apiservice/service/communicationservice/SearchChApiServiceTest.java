package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.SearchChCallCallServiceImpl;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.SearchChCallService;
import de.blackforestsolutions.apiservice.service.mapper.JourneyStatusBuilder;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class SearchChApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = Mockito.mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService = new SearchChHttpCallBuilderServiceImpl();

    private final SearchChCallService searchChCallService = new SearchChCallCallServiceImpl(restTemplateBuilder);

    @Mock
    private SearchChMapperService searchChMapperService = Mockito.mock(SearchChMapperService.class);

    @InjectMocks
    private SearchChApiService classUnderTest = new SearchChApiServiceImpl(searchChCallService, searchChHttpCallBuilderService, searchChMapperService);

    @Test
    void test_getTravelPointForRouteFromApiWith_mocked_mapper_returns_hashMap() throws IOException {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getSearchChStationTokenAndUrlIT();
        String stationJson = TestUtils.getResourceFileAsString("json/searchChTestStation.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(stationJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        Mockito.doReturn(testResult).when(REST_TEMPLATE).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Class.class));
        Map<String, TravelPoint> mockedStationMap = new HashMap<>();
        TravelPoint mockedTravelPoint = TravelPointObjectMother.getEinsiedelnTravelPoint();
        mockedStationMap.put(mockedTravelPoint.getStationId(), mockedTravelPoint);
        Mockito.when(searchChMapperService.getTravelPointFrom(Mockito.anyString())).thenReturn(mockedStationMap);

        Map<String, TravelPoint> result = classUnderTest.getTravelPointForRouteFromApiWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(mockedTravelPoint.getStationId())).isEqualToComparingFieldByField(mockedTravelPoint);
        Mockito.verify(searchChMapperService, Mockito.times(1)).getTravelPointFrom(stationJson);
    }


    @Test
    void test_getJourneyFromRouteWith_mocked_mapper_returns_hashMap() throws ParseException {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getSearchChRouteTokenAndUrlIT();
        String routeResourceJson = TestUtils.getResourceFileAsString("json/searchChTestRoute.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(routeResourceJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        Mockito.doReturn(testResult).when(REST_TEMPLATE).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Class.class));
        HashMap<UUID, JourneyStatus> mockedJourneys = new HashMap<>();
        Journey mockedJourney = JourneyObjectMother.getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey();
        mockedJourneys.put(mockedJourney.getId(), JourneyStatusBuilder.createJourneyStatusWith(mockedJourney));
        Mockito.when(searchChMapperService.getJourneysFrom(Mockito.anyString())).thenReturn(mockedJourneys);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.size()).isEqualTo(1);
        //noinspection OptionalGetWithoutIsPresent (justification: we allways know that the optional here is not empty)
        Assertions.assertThat(result.get(mockedJourney.getId()).getJourney().get()).isEqualToComparingFieldByField(mockedJourney);
        Mockito.verify(searchChMapperService, Mockito.times(1)).getJourneysFrom(routeResourceJson);
    }

}
