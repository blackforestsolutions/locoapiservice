package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.Status;

import java.util.*;

public class CallStatusListObjectMother {

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusList() {
        List<CallStatus<Map<UUID, JourneyStatus>>> callStatuses = new ArrayList<>();
        Problem problem = new Problem.ProblemBuilder().setExceptions(List.of(new Exception("Test JourneyStatus"))).build();
        JourneyStatus journeyStatusProblem = new JourneyStatus();
        journeyStatusProblem.setProblem(Optional.of(problem));
        journeyStatusProblem.setJourney(Optional.empty());
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney()));
        journeyStatus.setProblem(Optional.empty());
        CallStatus<Map<UUID, JourneyStatus>> callStatusSuccess = new CallStatus<>(Collections.singletonMap(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId(), journeyStatus), Status.SUCCESS, null);
        CallStatus<Map<UUID, JourneyStatus>> callStatusFailure = new CallStatus<>(Collections.emptyMap(), Status.FAILED, new Exception("Test CallStatus"));
        callStatuses.add(callStatusSuccess);
        callStatuses.add(callStatusFailure);
        return callStatuses;
    }
}
