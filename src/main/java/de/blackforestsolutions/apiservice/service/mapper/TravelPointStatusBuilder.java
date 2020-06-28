package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.List;
import java.util.Optional;

public class TravelPointStatusBuilder {

    public static TravelPointStatus createTravelPointStatusWith(TravelPoint travelPoint) {
        TravelPointStatus travelPointStatus = new TravelPointStatus();
        travelPointStatus.setTravelPoint(Optional.of(travelPoint));
        travelPointStatus.setProblem(Optional.empty());
        return travelPointStatus;
    }

    public static TravelPointStatus createTravelPointStatusProblemWith(List<Exception> exceptions, List<String> loggedMessages) {
        TravelPointStatus travelPointStatus = new TravelPointStatus();
        travelPointStatus.setTravelPoint(Optional.empty());
        travelPointStatus.setProblem(Optional.of(solve(exceptions, loggedMessages)));
        return travelPointStatus;
    }

    private static Problem solve(List<Exception> exceptions, List<String> loggedMessages) {
        Problem.ProblemBuilder builder = new Problem.ProblemBuilder();
        builder.setExceptions(exceptions);
        builder.setLoggedMessages(loggedMessages);
        return new Problem(builder.build());
    }
}
