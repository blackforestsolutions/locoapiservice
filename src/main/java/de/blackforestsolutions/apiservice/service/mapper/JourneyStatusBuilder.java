package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Problem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JourneyStatusBuilder {

    public static JourneyStatus createJourneyStatusWith(Journey journey) {
        return new JourneyStatus(Optional.of(journey), Optional.empty());
    }

    public static JourneyStatus createJourneyStatusProblemWith(List<Exception> exceptions, List<String> loggedMessages) {
        return new JourneyStatus(
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

    public static UUID extractJourneyUuidFrom(JourneyStatus journeyStatus) {
        if (journeyStatus.getJourney().isEmpty()) {
            log.error("No UUID available");
            return null;
        } else {
            return journeyStatus.getJourney().get().getId();
        }
    }
}
