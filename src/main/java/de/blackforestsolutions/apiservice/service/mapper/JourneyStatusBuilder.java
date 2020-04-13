package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Problem;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.EMPTY_LIST;

@Slf4j
public class JourneyStatusBuilder {

    public static JourneyStatus createJourneyStatusWith(Journey journey) {
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(journey));
        journeyStatus.setProblem(Optional.empty());
        return journeyStatus;
    }

    public static JourneyStatus createJourneyStatusProblemWith(Exception exception) {
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.empty());
        journeyStatus.setProblem(Optional.of(solve(exception)));
        return journeyStatus;
    }

    private static Problem solve(Exception exception) {
        Problem.ProblemBuilder builder = new Problem.ProblemBuilder();
        builder.setExceptions(Collections.singletonList(exception));
        //noinspection unchecked (justification: we will not have a list of logmessages at this place)
        builder.setLoggedMessages(EMPTY_LIST);
        return new Problem(builder.build());
    }

    public static UUID extractJourneyUuidFrom(JourneyStatus journeyStatus) {
        if (journeyStatus.getJourney().isEmpty()) {
            log.error("No UUID available");
            return null;
        } else {
            return journeyStatus.getJourney().get().getId();
        }
    }
}