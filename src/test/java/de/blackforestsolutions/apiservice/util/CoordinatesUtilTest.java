package de.blackforestsolutions.apiservice.util;

import de.blackforestsolutions.datamodel.Coordinates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CoordinatesUtilTest {

    /**
     * testData = Zuerich Foerlibuckstreet.
     */
    @Test
    void test_convertCh1903ToCoordinatesWith_x_and_y_returns_correct_coordinates() {

        Coordinates result = CoordinatesUtil.convertCh1903ToCoordinatesWith(666223, 211383);

        Assertions.assertThat(result.getLatitude()).isEqualTo(51.016962568215476);
        Assertions.assertThat(result.getLongitude()).isEqualTo(1.9118631730189604);
    }

    /**
     * testData = Hamburg Hbf Coordinates
     */
    @Test
    void test_convertWGS84ToCoordinatesWith_x_and_y_returns_correct_coordinates() {

        Coordinates result = CoordinatesUtil.convertWGS84ToCoordinatesWith(10006909, 53552733);

        Assertions.assertThat(result.getLatitude()).isEqualTo(53.552733);
        Assertions.assertThat(result.getLongitude()).isEqualTo(10.006909);
    }
}
