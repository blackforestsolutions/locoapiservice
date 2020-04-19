package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.AirportConfiguration;
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
    private Set<TravelPoint> result;

    public AirportsFinderMapperServiceTest() throws IOException {
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void test_map_jsonObject_and_return_Set_With_TravelPoints() {
        String airportsFinderResource = getResourceFileAsString("json/AirportsFinderJsons/FromTriberg300KmOnlyTwo.json");
        ArrayList<TravelPoint> testDataArrayList = getTravelPointsForAirportsFinder();
        Set<TravelPoint> result = classUnderTest.map(airportsFinderResource);
        ArrayList<TravelPoint> resultArrayList = convertSetToArrayListFortestingPurpose(result);
        Assertions.assertThat(result.size()).isEqualTo(2);

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

      /*  Assertions.assertThat(resultArrayList.get(0))
                .extracting(
                        TravelPoint::getAirportId,
                        TravelPoint::getCity,
                        TravelPoint::getAirportName,
                        //TravelPoint::getGpsCoordinates,
                        TravelPoint::getCountry
                ).contains(
                Tuple.tuple(
                        testDataArrayList.get(0).getAirportId(),
                        testDataArrayList.get(0).getCity(),
                        testDataArrayList.get(0).getAirportName(),
                        //testDataArrayList.get(0).getGpsCoordinates(),
                        testDataArrayList.get(0).getCountry()
                ));*/

       /*
        Assertions.assertThat(resultArrayList.get(1))
                .extracting(
                        TravelPoint::getAirportId,
                        TravelPoint::getCity,
                        TravelPoint::getAirportName,
                        TravelPoint::getGpsCoordinates,
                        TravelPoint::getCountry
                        //TravelPoint::getGpsCoordinates
                ).contains(
                Tuple.tuple(
                        testDataArrayList.get(1).getAirportId(),
                        testDataArrayList.get(1).getCity(),
                        testDataArrayList.get(1).getAirportName(),
                        testDataArrayList.get(1).getGpsCoordinates(),
                        testDataArrayList.get(1).getCountry()
                ));
*/
        //result.forEach(travelPoint ->  Assertions.assertThat("VTL").isisEqualToTo(travelPoint.getAirportId()));

    }

    private ArrayList<TravelPoint> convertSetToArrayListFortestingPurpose(Set<TravelPoint> set) {
        return new ArrayList<>(set);
    }
}
