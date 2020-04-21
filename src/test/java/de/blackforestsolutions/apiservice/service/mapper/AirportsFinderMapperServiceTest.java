package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;


public class AirportsFinderMapperServiceTest {


    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService classUnderTest = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    public AirportsFinderMapperServiceTest() throws IOException {
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void test_map_jsonObject_and_return_Set_With_TravelPoints() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyTwo.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        Set<CallStatus> result = classUnderTest.map(airportsFinderResource);
        ArrayList<CallStatus> resultArrayList = convertSetToArrayListForTestingPurpose(result);
        Assertions.assertThat(result.size()).isEqualTo(2);

        Assertions.assertThat(resultArrayList.get(0).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        Assertions.assertThat(resultArrayList.get(1).getCalledObject()).isEqualToComparingFieldByField(testDataArrayList.get(1));
    }

    private ArrayList<CallStatus> convertSetToArrayListForTestingPurpose(Set<CallStatus> set) {
        return new ArrayList<>(set);
    }

    @Test
    public void test_retrieveAirportsFinding_returns_callStatus_with_failedStatus() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/callStatusFailedWithEmptyJson.json");
        Set<CallStatus> result = classUnderTest.map(airportsFinderResource);

    }
}
