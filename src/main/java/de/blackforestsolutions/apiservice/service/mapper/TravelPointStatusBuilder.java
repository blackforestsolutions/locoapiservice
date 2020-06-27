package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Problem;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;

public class TravelPointStatusBuilder {

    public static TravelPointStatus createTravelPointStatusWith(TravelPoint travelPoint) {
        TravelPointStatus travelPointStatus = new TravelPointStatus();
        travelPointStatus.setTravelPoint(Optional.of(travelPoint));
        travelPointStatus.setProblem(Optional.empty());
        return travelPointStatus;
    }

    public static TravelPointStatus createTravelPointStatusProblemWith(Exception e) {
        TravelPointStatus travelPointStatus = new TravelPointStatus();
        travelPointStatus.setTravelPoint(Optional.empty());
        travelPointStatus.setProblem(Optional.of(solve(e)));
        return travelPointStatus;
    }

    private static Problem solve(Exception e) {
        Problem.ProblemBuilder builder = new Problem.ProblemBuilder();
        builder.setExceptions(Collections.singletonList(e));
        builder.setLoggedMessages(EMPTY_LIST);
        return new Problem(builder.build());
    }
}
