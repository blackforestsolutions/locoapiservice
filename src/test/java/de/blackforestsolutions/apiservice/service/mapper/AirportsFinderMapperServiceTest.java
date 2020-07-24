package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getTravelPointsForAirportsFinder;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;


class AirportsFinderMapperServiceTest {


    private final AirportConfiguration airportConfiguration = new AirportConfiguration();

    private final AirportsFinderMapperService classUnderTest = new AirportsFinderMapperServiceImpl(airportConfiguration.airports());

    AirportsFinderMapperServiceTest() throws IOException {
    }

    @Test
    void test_map_jsonObject_and_return_linkedHashSet_with_two_successful_and_one_failed_callstatus() throws JsonProcessingException {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/fromTriberg300KmOnlyThree.json");
        List<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();

        LinkedHashSet<TravelPointStatus> result = classUnderTest.map(airportsFinderResource);

        assertThat(new ArrayList<>(result).get(0).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(0));
        assertThat(new ArrayList<>(result).get(1).getTravelPoint().get()).isEqualToComparingFieldByField(testDataArrayList.get(1));
        assertThat(new ArrayList<>(result).get(2).getProblem().get().getExceptions().get(0)).isInstanceOf(Exception.class);
    }

}
