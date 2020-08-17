package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.Status;

import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;

public class CallStatusListObjectMother {

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusList() {
        UUID journeyStatusId = getBerlinHbfToHamburgLandwehrJourney().getId();
        JourneyStatus journeyStatus = new JourneyStatus(
                Optional.of(getBerlinHbfToHamburgLandwehrJourney()),
                Optional.empty()
        );
        return List.of(new CallStatus<>(Collections.singletonMap(journeyStatusId, journeyStatus), Status.SUCCESS, null));
    }

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusListFailed() {
        List<CallStatus<Map<UUID, JourneyStatus>>> callStatuses = new ArrayList<>();
        CallStatus<Map<UUID, JourneyStatus>> callStatusFailure = new CallStatus<>(null, Status.FAILED, new Exception("Test CallStatus"));
        callStatuses.add(callStatusFailure);
        return callStatuses;
    }

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusListSuccessWithEmptyMapAndException() {
        return List.of(
                new CallStatus<>(Collections.emptyMap(), Status.SUCCESS, new Exception("Test CallStatus"))
        );
    }

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusListWithAllParametersAsNull() {
        //noinspection ConstantConditions { for test case }
        return List.of(
                new CallStatus<>(null, null, null)
        );
    }

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusListWithJourneyStatusProblem() {
        Problem problem = new Problem.ProblemBuilder().setExceptions(List.of(new Exception("Test"))).build();
        JourneyStatus journeyStatus = new JourneyStatus(
                Optional.empty(),
                Optional.of(problem)
        );
        return List.of(new CallStatus<>(Collections.singletonMap(TEST_UUID_1, journeyStatus), Status.SUCCESS, null));
    }

}
