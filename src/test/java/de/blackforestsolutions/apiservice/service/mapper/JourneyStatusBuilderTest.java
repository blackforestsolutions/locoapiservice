package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


class JourneyStatusBuilderTest {

    @Test
    void test_createJourneyStatusWith_journey_returns_status_with_journey() {
        //arrange
        Journey testData = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();
        //act
        JourneyStatus result = JourneyStatusBuilder.createJourneyStatusWith(testData);
        //assert
        //noinspection OptionalGetWithoutIsPresent(justification: will allways be there)
        Assertions.assertThat(result.getJourney().get()).isEqualToComparingFieldByField(testData);
        Assertions.assertThat(result.getProblem().isEmpty()).isTrue();
    }

    @Test
    void test_createJourneyStatusProblemWith_Exception_returns_status_with_exception() {
        //arrange
        Exception e = new Exception();
        //act
        JourneyStatus result = JourneyStatusBuilder.createJourneyStatusProblemWith(List.of(e), Collections.emptyList());
        //assert
        Assertions.assertThat(result.getJourney().isEmpty()).isTrue();
        Assertions.assertThat(result.getProblem().isEmpty()).isFalse();
    }

    @Test
    void extractJourneyUuidFrom() {
        //arrange
        Journey testJourney = JourneyObjectMother.getGustavHeinemannStreetToUniversityJourney();
        JourneyStatus testJourneyStatus = JourneyStatusBuilder.createJourneyStatusWith(testJourney);
        //act
        UUID result = JourneyStatusBuilder.extractJourneyUuidFrom(testJourneyStatus);
        //assert
        Assertions.assertThat(result).isEqualTo(testJourney.getId());
    }
}