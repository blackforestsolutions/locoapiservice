package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.OSMCallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.OSMCallServiceImpl;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.OSMHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;

public class OSMApiServiceTest {
    private static final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(restTemplate);

    private final OSMCallService osmCallService = new OSMCallServiceImpl(restTemplateBuilder);

    private final OSMHttpCallBuilderService osmHttpCallBuilderService = new OSMHttpCallBuilderServiceImpl();

    private final OSMApiService classUnderTest = new OSMApiServiceImpl(osmCallService, osmHttpCallBuilderService);

    @Test
    public void test_getCoordinatesFromTavelpointWith_mocked_json_apiToken_return_correct_departure_coordinates() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrlIT();
        String departureJson = getResourceFileAsString("json/osmTravelPointDeparture.json");
        ResponseEntity<String> testResultDeparture = new ResponseEntity<>(departureJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        Mockito.doReturn(testResultDeparture).when(restTemplate).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Class.class));

        CallStatus result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getDeparture());
        Coordinates coordinatesResult = (Coordinates) result.getCalledObject();

        Assertions.assertEquals(48.80549925, coordinatesResult.getLatitude());
        Assertions.assertEquals(9.228576954173775, coordinatesResult.getLongitude());
    }

    @Test
    public void test_getCoordinatesFromTavelpointWith_mocked_json_apiToken_return_correct_arrival_coordinates() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getOSMApiTokenAndUrlIT();
        String arrivalJson = getResourceFileAsString("json/osmTravelPointArrival.json");
        ResponseEntity<String> testResultArrival = new ResponseEntity<>(arrivalJson, HttpStatus.OK);
        //noinspection unchecked (justification: no type known for runtime therefore)
        Mockito.doReturn(testResultArrival).when(restTemplate).exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Class.class));

        CallStatus result = classUnderTest.getCoordinatesFromTravelPointWith(testData, testData.getArrival());
        Coordinates coordinatesResult = (Coordinates) result.getCalledObject();

        Assertions.assertEquals(48.0510888, coordinatesResult.getLatitude());
        Assertions.assertEquals(8.2073542, coordinatesResult.getLongitude());
    }
}