package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.List;
import java.util.Optional;

public class TravelPointStatusBuilder {

    public static TravelPointStatus createTravelPointStatusWith(TravelPoint travelPoint) {
        return new TravelPointStatus(Optional.of(travelPoint), Optional.empty());
    }

    public static TravelPointStatus createTravelPointStatusProblemWith(List<Exception> exceptions, List<String> loggedMessages) {
        return new TravelPointStatus(
                Optional.empty(),
                Optional.of(solve(exceptions, loggedMessages))
        );
    }

    private static Problem solve(List<Exception> exceptions, List<String> loggedMessages) {
        Problem.ProblemBuilder builder = new Problem.ProblemBuilder();
        builder.setExceptions(exceptions);
        builder.setLoggedMessages(loggedMessages);
        return new Problem(builder.build());
    }
}
