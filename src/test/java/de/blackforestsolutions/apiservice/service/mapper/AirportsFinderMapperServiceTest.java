package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
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
    void test_map_jsonObject_and_return_linkedHashSet_with_two_successful_and_one_failed_callstatus() throws JsonProcessingException {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();

        LinkedHashSet<TravelPointStatus> resultLinkedHashSet = classUnderTest.map(airportsFinderResource);
        ArrayList<TravelPointStatus> resultArrayList = convertSetToArrayListForTestingPurpose(resultLinkedHashSet);

        Assertions.assertThat(resultArrayList.get(0).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        Assertions.assertThat(resultArrayList.get(1).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(1));
    }

    private ArrayList<TravelPointStatus> convertSetToArrayListForTestingPurpose(LinkedHashSet<TravelPointStatus> linkedHashSet) {
        return new ArrayList<>(linkedHashSet);
    }
}
