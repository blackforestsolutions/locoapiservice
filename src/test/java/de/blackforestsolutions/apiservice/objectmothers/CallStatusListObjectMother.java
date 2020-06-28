package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.Status;

import java.text.ParseException;
import java.util.*;

public class CallStatusListObjectMother {

    public static List<CallStatus<Map<UUID, JourneyStatus>>> retrieveCallStatusList() throws ParseException {
        List<CallStatus<Map<UUID, JourneyStatus>>> callStatuses = new ArrayList<>();
        Problem problem = new Problem.ProblemBuilder().setExceptions(Arrays.asList(new Exception("Test JourneyStatus"))).build();
        JourneyStatus journeyStatusProblem = new JourneyStatus();
        journeyStatusProblem.setProblem(Optional.of(problem));
        journeyStatusProblem.setJourney(Optional.empty());
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney()));
        journeyStatus.setProblem(Optional.empty());
        CallStatus<Map<UUID, JourneyStatus>> callStatusSuccess = new CallStatus<Map<UUID, JourneyStatus>>(Collections.singletonMap(JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney().getId(), journeyStatus), Status.SUCCESS, null);
        CallStatus<Map<UUID, JourneyStatus>> callStatusFailure = new CallStatus<Map<UUID, JourneyStatus>>(Collections.EMPTY_MAP, Status.FAILED, new Exception("Test CallStatus"));
        callStatuses.add(callStatusSuccess);
        callStatuses.add(callStatusFailure);
        return callStatuses;
    }
}
