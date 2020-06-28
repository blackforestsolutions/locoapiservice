package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class TravelPointStatusBuilderTest {

    @Test
    void test_createTravelPointStatusWith_returns_travelPoint_with_status() {
        TravelPoint testData = TravelPointObjectMother.getBerlinFlughafenTravelPoint();

        TravelPointStatus result = TravelPointStatusBuilder.createTravelPointStatusWith(testData);

        Assertions.assertThat(result.getTravelPoint().get()).isEqualToComparingFieldByField(testData);
        Assertions.assertThat(result.getProblem().isEmpty()).isTrue();
    }

    @Test
    void test_createTravelPointStatusProblemWith_exceptions_returns_status_with_exception() {
        Exception exception = new Exception();

        TravelPointStatus result = TravelPointStatusBuilder.createTravelPointStatusProblemWith(List.of(exception), Collections.emptyList());

        Assertions.assertThat(result.getTravelPoint().isEmpty()).isTrue();
        Assertions.assertThat(result.getProblem().isEmpty()).isFalse();
    }
}
