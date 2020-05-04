package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HvvCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HvvCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.HvvMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.*;
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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.Mockito.*;

class HvvApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final HvvCallService hvvCallService = new HvvCallServiceImpl(restTemplateBuilder);

    private final HvvJourneyHttpCallBuilderService journeyHttpCallBuilder = new HvvJourneyHttpCallBuilderServiceImpl();

    private final HvvTravelPointHttpCallBuilderService travelPointHttpCallBuilder = new HvvTravelPointHttpCallBuilderServiceImpl();

    private final HvvStationListHttpCallBuilderService stationListHttpCallBuilder = new HvvStationListHttpCallBuilderServiceImpl();

    @Mock
    private HvvMapperService mapperService = mock(HvvMapperService.class);

    @InjectMocks
    private HvvApiService classUnderTest = new HvvApiServiceImpl(
            hvvCallService,
            stationListHttpCallBuilder,
            travelPointHttpCallBuilder,
            journeyHttpCallBuilder,
            mapperService
    );

    @Test
    void test_getJourneysForRouteFromHvvApiWith_with_mocked_rest_and_json_is_excuted_correctly_and_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getHvvJourneyTokenAndUrl();
        String rotenhofTravelPointJson = TestUtils.getResourceFileAsString("json/hvvRotenhofTravelPoint.json");
        ResponseEntity<String> rotenhofTravelPointResult = new ResponseEntity<>(rotenhofTravelPointJson, HttpStatus.OK);
        String stadthausbrueckeTravelPointJson = TestUtils.getResourceFileAsString("json/hvvStadthausbrueckeTravelPoint.json");
        ResponseEntity<String> stadthausbrueckeTravelPointResult = new ResponseEntity<>(stadthausbrueckeTravelPointJson, HttpStatus.OK);
        String journeyJson = TestUtils.getResourceFileAsString("json/hvvJourney.json");
        ResponseEntity<String> journeyTestResult = new ResponseEntity<>(journeyJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        when(REST_TEMPLATE.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(rotenhofTravelPointResult).thenReturn(stadthausbrueckeTravelPointResult).thenReturn(journeyTestResult);
        HashMap<UUID, JourneyStatus> mockedJourneys = new HashMap<>();
        Journey mockedJourney = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(mockedJourney));
        mockedJourneys.put(mockedJourney.getId(), journeyStatus);
        when(mapperService.getJourneyMapFrom(journeyJson)).thenReturn(mockedJourneys);

        Map<UUID, JourneyStatus> result = classUnderTest.getJourneysForRouteWith(apiTokenAndUrlInformation);

        Assertions.assertThat(result.size()).isEqualTo(1);
        //noinspection OptionalGetWithoutIsPresent (justification: we allways knoww that there optional here is not empty)
        Assertions.assertThat(result.get(mockedJourney.getId()).getJourney().get()).isEqualToComparingFieldByField(mockedJourney);
        verify(mapperService, times(1)).getJourneyMapFrom(journeyJson);
    }

    @Test
    void test_getStationListFromHvvApiWith_with_mocked_rest_is_excuted_correctly_and_returns_map() throws Exception {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();
        String stationListJson = TestUtils.getResourceFileAsString("json/hvvStationList.json");
        ResponseEntity<String> testResult = new ResponseEntity<>(stationListJson, HttpStatus.OK);
        //noinspection unchecked
        doReturn(testResult).when(REST_TEMPLATE).exchange(anyString(), any(), any(), any(Class.class));
        List<TravelPoint> mockedTravelPointsList = Arrays.asList(
                TravelPointObjectMother.getHvvHauptbahnhofTravelPoint(),
                TravelPointObjectMother.getPinnebergRichardKoehnHvvTravelPoint()
        );
        when(mapperService.getStationListFrom(any())).thenReturn(mockedTravelPointsList);

        List<TravelPoint> result = (List<TravelPoint>) classUnderTest.getStationListFromHvvApiWith(apiTokenAndUrlInformation).getCalledObject();

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(TravelPointObjectMother.getHvvHauptbahnhofTravelPoint());
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(TravelPointObjectMother.getPinnebergRichardKoehnHvvTravelPoint());
        verify(mapperService, times(1)).getStationListFrom(stationListJson);
    }

}
