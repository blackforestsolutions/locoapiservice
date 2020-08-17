package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.Coordinates;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.objectmothers.BoxObjectMother.getTestBox;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoxServiceTest {

    private final BoxService classUnderTest = new BoxServiceImpl();

    @Test
    void test_isCoordinateInBox_with_test_coordinates_in_test_box_returns_true() {
        Coordinates testCoordinates = new Coordinates.CoordinatesBuilder(50d, 50d).build();

        boolean result = classUnderTest.isCoordinateInBox(testCoordinates, getTestBox());

        assertTrue(result);
    }

    @Test
    void test_isCoordinateInBox_with_test_coordinates_not_in_test_box_returns_true() {
        Coordinates testCoordinates = new Coordinates.CoordinatesBuilder(200, 200).build();

        boolean result = classUnderTest.isCoordinateInBox(testCoordinates, getTestBox());

        assertFalse(result);
    }
}
