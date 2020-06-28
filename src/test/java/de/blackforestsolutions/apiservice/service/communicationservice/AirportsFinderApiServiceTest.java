package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperService;
import de.blackforestsolutions.apiservice.service.mapper.AirportsFinderMapperServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.AirportsFinderHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getAirportsFinderTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class AirportsFinderApiServiceTest {
    private static final RestTemplate restTemplate = mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService mapper = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    private final AirportsFinderHttpCallBuilderService httpCallBuilderService = new AirportsFinderHttpCallBuilderServiceImpl();

    private final AirportsFinderApiService classUnderTest = new AirportsFinderApiServiceImpl(callService, httpCallBuilderService, mapper);

    public AirportsFinderApiServiceTest() throws IOException {
    }

    @Test
    public void test_getAirportsAsTravelPoints_with_mocked_rest_service_is_executed_correctly_and_maps_correctly_returns_linkedHashSet() throws JsonProcessingException {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getAirportsFinderTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        apiTokenAndUrlInformation = builder.build();
        ResponseEntity<String> testResult = new ResponseEntity<>(airportsFinderResource, HttpStatus.OK);
        doReturn(testResult).when(restTemplate).exchange(anyString(), any(), Mockito.any(), any(Class.class));

        CallStatus<LinkedHashSet<TravelPointStatus>> result = classUnderTest.getAirportsWith(apiTokenAndUrlInformation);

        Assertions.assertThat(new ArrayList<>(result.getCalledObject()).size()).isEqualTo(3);
        Assertions.assertThat(new ArrayList<>(result.getCalledObject()).get(0).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        Assertions.assertThat(new ArrayList<>(result.getCalledObject()).get(1).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(1));
        Assertions.assertThat(new ArrayList<>(result.getCalledObject()).get(2).getTravelPoint().isEmpty()).isTrue();
    }
}