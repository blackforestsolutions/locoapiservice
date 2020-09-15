package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import de.blackforestsolutions.apiservice.util.CoordinatesUtil;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.generatedcontent.blablabus.response.BlaBlaBusStops;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.apiservice.service.mapper.MapperService.checkIfStringPropertyExists;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

class MapperServiceTest {

    @Test
    void test_checkIfStringPropertyExists_with_not_empty_testWord_returns_same_word() {
        String testData = "testWord";

        String result = checkIfStringPropertyExists(testData);

        assertThat(result).isEqualTo(testData);
    }

    @Test
    void test_checkIfStringPropertyExists_with_empty_testword_returns_empty_string() {
        String testData = null;

        //noinspection ConstantConditions (justification: testdata is allowed to be null)
        String result = checkIfStringPropertyExists(testData);

        assertThat(result).isEqualTo("");
    }

    @Test
    void test_converting_ch1903_coordinates_delivers_correct_coordinates() {
        Coordinates coordinates = CoordinatesUtil.convertCh1903ToCoordinatesWith(666223, 211383);
        assertThat(new Coordinates.CoordinatesBuilder(51.016962568215476, 1.9118631730189604).build()).isEqualTo(coordinates);
    }

    @Test
    void test_convertJsonToPojo_returns_pojo_for_blaBlaBlaBusStops_correctly() {
        String testJson = getResourceFileAsString("json/blaBlaBusStops.json");

        Mono<BlaBlaBusStops> result = MapperService.convertJsonToPojo(testJson, BlaBlaBusStops.class);

        StepVerifier.create(result)
                .assertNext(blaBlaBusStops -> {
                    assertThat(blaBlaBusStops.getStops().size()).isEqualTo(3);
                    assertThat(blaBlaBusStops.getStops().get(2).getStops().size()).isEqualTo(3);
                })
                .verifyComplete();
    }

    @Test
    void test_convertJsonToPojo_returns_mono_with_error_when_json_is_not_valid() {

        Mono<BlaBlaBusStops> result = MapperService.convertJsonToPojo("", BlaBlaBusStops.class);

        StepVerifier.create(result)
                .expectError(MismatchedInputException.class)
                .verify();
    }
}
