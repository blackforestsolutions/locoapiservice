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


class AirportsFinderMapperServiceTest {


    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService classUnderTest = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    AirportsFinderMapperServiceTest() throws IOException {
    }

    @Test
    void test_map_jsonObject_and_return_linkedHashSet_with_two_successful_and_one_failed_callstatus() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        LinkedHashSet<CallStatus<TravelPoint>> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus<TravelPoint>> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);

        Assertions.assertThat(resultArrayList.get(0).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        Assertions.assertThat(resultArrayList.get(1).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(1));
        Assertions.assertThat(resultArrayList.get(2).getCalledObject()).isEqualTo(null);
        Assertions.assertThat(resultArrayList.get(2).getStatus()).isEqualTo(Status.FAILED);
        Assertions.assertThat(resultArrayList.get(2).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat");
    }

    @Test
    void test_map_jsonObject_with_airportCode_as_null_and_return_callStatus_with_null() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithExceptions.json");
        LinkedHashSet<CallStatus<TravelPoint>> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus<TravelPoint>> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);
        Assertions.assertThat(resultArrayList.get(0).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code appears to be null");
    }

    @Test
    void test_map_jsonObject_with_airportCode_as_not_an_airportId_and_return_callStatus_with_airportCode_of_no_airport_in_airportBat() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithExceptions.json");
        LinkedHashSet<CallStatus<TravelPoint>> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus<TravelPoint>> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);
        Assertions.assertThat(resultArrayList.get(2).getException().getMessage()).isEqualTo("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat");
    }

    private ArrayList<CallStatus<TravelPoint>> convertSetToArrayListForTestingPurpose(LinkedHashSet<CallStatus<TravelPoint>> linkedHashSet) {
        return new ArrayList<>(linkedHashSet);
    }
}
