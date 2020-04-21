package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.AirportsFinderCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.AirportsFinderCallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperService;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrlIT;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class AirportsFinderApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final AirportsFinderCallService lufthansaCallService = new AirportsFinderCallServiceImpl(restTemplateBuilder);

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService mapper = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    private final AirportsFinderHttpCallBuilderService httpCallBuilderService = new AirportsFinderHttpCallBuilderServiceImpl();

    private final AirportsFinderApiService classUnderTest = new AirportsFinderApiServiceImpl(lufthansaCallService, httpCallBuilderService, mapper);

    public AirportsFinderApiServiceTest() throws IOException {
    }

    @Test
    public void test_getAirportsAsTravelPoints_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_map() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyTwo.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();

        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getAirportsFinderTokenAndUrlIT();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        apiTokenAndUrlInformation = builder.build();

        ResponseEntity<String> testResult = new ResponseEntity<>(airportsFinderResource, HttpStatus.OK);
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), Mockito.any(), any(Class.class));
        Set<TravelPoint> resultSet = classUnderTest.getAirportsWith(apiTokenAndUrlInformation);

        ArrayList<TravelPoint> resultArrayList = convertSetToArrayListFortestingPurpose(resultSet);
        assertThat(resultSet.size()).isEqualTo(2);

        Assertions.assertThat(resultArrayList.get(0).getAirportId()).isEqualTo(testDataArrayList.get(0).getAirportId());
        Assertions.assertThat(resultArrayList.get(0).getAirportId()).isEqualTo(testDataArrayList.get(0).getAirportId());
        Assertions.assertThat(resultArrayList.get(0).getCity()).isEqualTo(testDataArrayList.get(0).getCity());
        Assertions.assertThat(resultArrayList.get(0).getAirportName()).isEqualTo(testDataArrayList.get(0).getAirportName());
        Assertions.assertThat(Double.toString(resultArrayList.get(0).getGpsCoordinates().getLongitude())).isEqualTo(Double.toString(testDataArrayList.get(0).getGpsCoordinates().getLongitude()));
        Assertions.assertThat(Double.toString(resultArrayList.get(0).getGpsCoordinates().getLatitude())).isEqualTo(Double.toString(testDataArrayList.get(0).getGpsCoordinates().getLatitude()));
        Assertions.assertThat(resultArrayList.get(0).getCountry()).isEqualTo(testDataArrayList.get(0).getCountry());
        Assertions.assertThat(resultArrayList.get(0).getAirportId()).isEqualTo(testDataArrayList.get(0).getAirportId());

        Assertions.assertThat(resultArrayList.get(1).getAirportId()).isEqualTo(testDataArrayList.get(1).getAirportId());
        Assertions.assertThat(resultArrayList.get(1).getCity()).isEqualTo(testDataArrayList.get(1).getCity());
        Assertions.assertThat(resultArrayList.get(1).getAirportName()).isEqualTo(testDataArrayList.get(1).getAirportName());
        Assertions.assertThat(Double.toString(resultArrayList.get(1).getGpsCoordinates().getLongitude())).isEqualTo(Double.toString(testDataArrayList.get(1).getGpsCoordinates().getLongitude()));
        Assertions.assertThat(Double.toString(resultArrayList.get(1).getGpsCoordinates().getLatitude())).isEqualTo(Double.toString(testDataArrayList.get(1).getGpsCoordinates().getLatitude()));
        Assertions.assertThat(resultArrayList.get(1).getCountry()).isEqualTo(testDataArrayList.get(1).getCountry());
        Assertions.assertThat(resultArrayList.get(1).getAirportId()).isEqualTo(testDataArrayList.get(1).getAirportId());
    }

    private ArrayList<TravelPoint> convertSetToArrayListFortestingPurpose(Set<TravelPoint> set) {
        return new ArrayList<>(set);
    }
}