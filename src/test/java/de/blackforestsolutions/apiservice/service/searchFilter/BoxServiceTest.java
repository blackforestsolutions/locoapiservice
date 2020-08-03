package de.blackforestsolutions.apiservice.service.searchFilter;

import de.blackforestsolutions.apiservice.objectmothers.BoxObjectMother;
import de.blackforestsolutions.apiservice.service.searchfilter.BoxService;
import de.blackforestsolutions.apiservice.service.searchfilter.BoxServiceImpl;
import de.blackforestsolutions.datamodel.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Box;

import static org.assertj.core.api.Assertions.assertThat;

public class BoxServiceTest {

    private final Box boxMock = Mockito.mock(Box.class);

    private final BoxService classUnderTest = new BoxServiceImpl(this.boxMock);
    //AirportConfiguration . LufthansaMapperServiceImpl anschauen wegen Bean

    void init() {
        //Mockito.when(this.boxMock).thenReturn(BoxObjectMother.rmvBox());
        //Mockito.when(boxMock.getFirst()).thenReturn(BoxObjectMother.fist()); für spätere Testfälle
        Mockito.when(this.boxMock.getFirst().getX()).thenReturn(BoxObjectMother.TEST_POINT_1.getX());
        Mockito.when(this.boxMock.getFirst().getX()).thenReturn(BoxObjectMother.TEST_POINT_1.getX());
        Mockito.when(this.boxMock.getSecond()).thenReturn(BoxObjectMother.TEST_POINT_2);

    }

    @Test
    void test_checkIfProviderIsInRangeWiths_boxFromFrankfurtToStuttgart_with_position_in_mosbach() {

        boolean result = this.classUnderTest.checkIfProviderIsInRangeWiths(
                new Coordinates.CoordinatesBuilder(49.335309, 9.112023).build()
        );
        assertThat("true").isEqualTo(result);
        //Assertions.assertTrue("", res);
    }

    @Test
    void test_checkIfProviderIsInRangeWiths_boxFromFrankfurtToStuttgart_with_position_in_munich() {

        boolean result = classUnderTest.checkIfProviderIsInRangeWiths(
                new Coordinates.CoordinatesBuilder(48.117574, 11.603394).build()
        );
        Assertions.assertFalse(result);
    }
}
