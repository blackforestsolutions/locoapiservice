package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;


public class AirportsFinderMapperServiceTest {


    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService classUnderTest = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    public AirportsFinderMapperServiceTest() throws IOException {
    }

    @Test
    public void test_map_jsonObject_and_return_linkedHashSet_with_two_successful_and_one_failed_callstatus() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        LinkedHashSet<CallStatus> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);

        Assertions.assertThat(resultArrayList.get(0).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        Assertions.assertThat(resultArrayList.get(1).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(1));
        Assertions.assertThat(resultArrayList.get(2).getCalledObject()).isEqualTo(null);
        Assertions.assertThat(resultArrayList.get(2).getStatus()).isEqualTo(Status.FAILED);
        Assertions.assertThat(resultArrayList.get(2).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat");
    }

    @Test
    public void test_map_jsonObject_with_airportCode_as_null_and_return_callStatus_with_nullPointerException() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithExceptions.json");
        LinkedHashSet<CallStatus> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);

        resultArrayList.get(0).getException();
        // todo can't get npe
    }

    @Test
    public void test_map_jsonObject_with_airportCode_as_null_and_return_callStatus_with_null() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithExceptions.json");
        LinkedHashSet<CallStatus> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);
        Assertions.assertThat(resultArrayList.get(0).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code appears to be null");
    }

    @Test
    public void test_map_jsonObject_with_airportCode_as_not_an_airportId_and_return_callStatus_with_airportCode_of_no_airport_in_airportBat() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithExceptions.json");
        LinkedHashSet<CallStatus> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);
        Assertions.assertThat(resultArrayList.get(2).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat");
    }

    private ArrayList<CallStatus> convertSetToArrayListForTestingPurpose(LinkedHashSet<CallStatus> linkedHashSet) {
        return new ArrayList<>(linkedHashSet);
    }
}
