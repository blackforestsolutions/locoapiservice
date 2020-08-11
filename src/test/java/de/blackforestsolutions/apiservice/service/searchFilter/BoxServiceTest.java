package de.blackforestsolutions.apiservice.service.searchFilter;

import de.blackforestsolutions.apiservice.objectmothers.BoxObjectMother;
import de.blackforestsolutions.apiservice.service.searchfilter.BoxService;
import de.blackforestsolutions.apiservice.service.searchfilter.BoxServiceImpl;
import de.blackforestsolutions.datamodel.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Box;

public class BoxServiceTest {

    private final Box boxMock = Mockito.mock(Box.class, Mockito.RETURNS_DEEP_STUBS);
    private final BoxService classUnderTest = new BoxServiceImpl(this.boxMock);

    void init() {
        Mockito.when(this.boxMock.getFirst()).thenReturn(BoxObjectMother.TEST_TOP_LEFT_POINT_1);
        Mockito.when(this.boxMock.getSecond()).thenReturn(BoxObjectMother.TEST_BOTTOM_RIGHT_POINT_1);
    }

    @Test
    void test_checkIfProviderIsInRangeWiths_boxForRMV_with_position_inside_in_Frankfurt() {

        init();
        boolean result = this.classUnderTest.isProviderInRangeWiths(
                new Coordinates.CoordinatesBuilder(50.11, 8.41).build());
        Assertions.assertTrue(result);
    }

    @Test
    void test_checkIfProviderIsInRangeWiths_boxFromFrankfurtToStuttgart_with_position_outside_in_munich() {

        boolean result = classUnderTest.isProviderInRangeWiths(
                new Coordinates.CoordinatesBuilder(48.117574, 11.603394).build());
        Assertions.assertFalse(result);
    }
}
