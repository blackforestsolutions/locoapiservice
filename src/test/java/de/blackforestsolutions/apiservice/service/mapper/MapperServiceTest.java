package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.util.CoordinatesUtil;
import de.blackforestsolutions.datamodel.Coordinates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.checkIfStringPropertyExists;

class MapperServiceTest {

    @Test
    void test_checkIfStringPropertyExists_with_not_empty_testWord_returns_same_word() {
        String testData = "testWord";

        String result = checkIfStringPropertyExists(testData);

        Assertions.assertThat(result).isEqualTo(testData);
    }

    @Test
    void test_checkIfStringPropertyExists_with_empty_testword_returns_empty_string() {
        String testData = null;

        //noinspection ConstantConditions (justification: testdata is allowed to be null)
        String result = checkIfStringPropertyExists(testData);

        Assertions.assertThat(result).isEqualTo("");
    }

    @Test
    void test_converting_ch1903_coordinates_delivers_correct_coordinates() {
        Coordinates coordinates = CoordinatesUtil.convertCh1903ToCoordinatesWith(666223, 211383);
        Assertions.assertThat(new Coordinates.CoordinatesBuilder(51.016962568215476, 1.9118631730189604).build()).isEqualTo(coordinates);
    }
}
