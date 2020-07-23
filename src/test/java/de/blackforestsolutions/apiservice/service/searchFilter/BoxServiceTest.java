package de.blackforestsolutions.apiservice.service.searchFilter;

import de.blackforestsolutions.apiservice.service.searchfilter.BoxService;
import de.blackforestsolutions.apiservice.service.searchfilter.BoxServiceImpl;
import de.blackforestsolutions.datamodel.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoxServiceTest {
    private final BoxService classUnderTest = new BoxServiceImpl();

    @Test
    void test_checkIfProviderIsInRangeWiths_boxFromFrankfurtToStuttgart_with_position_in_mosbach() {

        boolean result = classUnderTest.checkIfProviderIsInRangeWiths(
                new Coordinates.CoordinatesBuilder(49.335309, 9.112023).build()
        );
        Assertions.assertTrue(result);
    }

    @Test
    void test_checkIfProviderIsInRangeWiths_boxFromFrankfurtToStuttgart_with_position_in_munich() {

        boolean result = classUnderTest.checkIfProviderIsInRangeWiths(
                new Coordinates.CoordinatesBuilder(48.117574, 11.603394).build()
        );
        Assertions.assertTrue(result);
    }
}
