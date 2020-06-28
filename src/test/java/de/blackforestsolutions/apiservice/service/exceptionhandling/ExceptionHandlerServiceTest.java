package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.apiservice.objectmothers.CallStatusListObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.datamodel.exception.CompromisedAttributeException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionHandlerServiceTest {

    private final ExceptionHandlerService classUnderTest = new ExceptionHandlerServiceServiceImpl();

    @Test
    void test_handleExceptionsWith_CallStatuses() throws ParseException, CompromisedAttributeException {
        List<CallStatus<Map<UUID, JourneyStatus>>> testData = CallStatusListObjectMother.retrieveCallStatusList();

        Map<UUID, Journey> result = this.classUnderTest.handleExceptions(testData);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId()).getId()).isEqualTo(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId());
        assertThat(result.get(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId()).getArrivalTimeFromJourney()).isEqualTo(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getArrivalTimeFromJourney());
        assertThat(result.get(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId()).getLegs().entrySet().size()).isEqualTo(1);
    }

    @Test
    void test_handleExceptionsTravelPoints_with_travelPoint() {
        TravelPoint travelPoint = TravelPointObjectMother.getHvvHauptbahnhofTravelPoint();
        TravelPointStatus travelPointStatus = new TravelPointStatus();
        travelPointStatus.setProblem(Optional.empty());
        travelPointStatus.setTravelPoint(Optional.of(travelPoint));
        LinkedHashSet<TravelPointStatus> travelPointStatuses = new LinkedHashSet<>();
        travelPointStatuses.add(travelPointStatus);
        CallStatus<LinkedHashSet<TravelPointStatus>> testData = new CallStatus<>(travelPointStatuses, Status.SUCCESS, null);

        LinkedHashSet<TravelPoint> result = this.classUnderTest.handleExceptionsTravelPoints(testData);

        assertThat(result.stream().findAny().get()).isEqualToComparingFieldByField(travelPoint);
    }

    @Test
    void test_handleExceptions_with_Coordinates_exception() {
        CallStatus<Coordinates> testData = new CallStatus<>(null, Status.FAILED, new Exception("Test"));

        Coordinates result = this.classUnderTest.handleExceptions(testData);

        assertThat(result).isNull();
    }

    @Test
    void test_handleExceptions_with_Coordinates() {
        Coordinates coordinates = new Coordinates.CoordinatesBuilder(6446, 6546).build();
        CallStatus<Coordinates> testData = new CallStatus<>(coordinates, Status.SUCCESS, null);

        Coordinates result = this.classUnderTest.handleExceptions(testData);

        assertThat(result).isEqualToComparingFieldByField(coordinates);
    }
}
