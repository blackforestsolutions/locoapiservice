package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import de.blackforestsolutions.datamodel.exception.NoExternalResultFoundException;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getEuropeTravelPoint;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStuttgartWaiblingerStreetTravelPoint;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

class OsmMapperServiceTest {

    private final OsmMapperService classUnderTest = new OsmMapperServiceImpl();

    @Test
    void test_mapOsmJsonToTravelPoint_with_mocked_json_returns_correctly_mapped_travelPoint() {
        TravelPoint expectedTravelPoint = getStuttgartWaiblingerStreetTravelPoint();
        String addressJson = getResourceFileAsString("json/osmTravelPointAddress.json");

        TravelPointStatus result = classUnderTest.mapOsmJsonToTravelPoint(addressJson);

        assertThat(result.getTravelPoint().get()).isEqualToComparingFieldByField(expectedTravelPoint);
    }

    @Test
    void test_mapOsmJsonToTravelPoint_with_mocked_json_and_empty_address_field_returns_correctly_mapped_travelPoint() {
        TravelPoint expectedTravelPoint = getEuropeTravelPoint();
        String europaJson = getResourceFileAsString("json/osmTravelPointEuropa.json");

        TravelPointStatus result = classUnderTest.mapOsmJsonToTravelPoint(europaJson);

        assertThat(result.getTravelPoint().get()).isEqualToComparingFieldByField(expectedTravelPoint);
    }

    @Test
    void test_getCoordinatesFromTravelPointWith_wrong_pojo_returns_one_problem_with_nullPointerException() {
        String arrivalJson = getResourceFileAsString("json/osmTravelPointError.json");

        TravelPointStatus result = classUnderTest.mapOsmJsonToTravelPoint(arrivalJson);

        assertThat(result.getProblem().get().getExceptions().get(0)).isInstanceOf(NullPointerException.class);
        assertThat(result.getProblem().get().getExceptions().size()).isEqualTo(1);
    }

    @Test
    void test_mapOsmJsonToTravelPoint_with_mocked_and_empty_json_returns_problem_with_noExternalResultFoundException() {
        TravelPointStatus result = classUnderTest.mapOsmJsonToTravelPoint("[]");

        assertThat(result.getProblem().get().getExceptions().get(0)).isInstanceOf(NoExternalResultFoundException.class);
        assertThat(result.getProblem().get().getExceptions().size()).isEqualTo(1);
    }

}
