package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.datamodel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExceptionHandlerServiceServiceImpl implements ExceptionHandlerService {

    private static void logErrorTravelPoint(TravelPointStatus travelPointStatus) {
        if (travelPointStatus.getProblem().isPresent()) {
            log.error("Mapping Error for travel point {}", travelPointStatus.getProblem().get().getExceptions());
        }
    }

    private static Map<UUID, Journey> handleCallStatus(Map<UUID, JourneyStatus> journeys) {
        return journeys.values()
                .stream()
                .peek(ExceptionHandlerServiceServiceImpl::logError)
                .filter(journeyStatus -> journeyStatus.getJourney().isPresent())
                .map(JourneyStatus::getJourney)
                .map(Optional::get)
                .collect(Collectors.toMap(Journey::getId, journey -> journey));
    }

    private static void logError(JourneyStatus journeyStatus) {
        if (journeyStatus.getProblem().isPresent()) {
            log.error("A mapping failed due to {}", journeyStatus.getProblem().get().getExceptions());
        }
    }

    @Override
    public Map<UUID, Journey> handleExceptions(List<CallStatus<Map<UUID, JourneyStatus>>> callStatusList) {
        try {
            return callStatusList.stream()
                    .peek(ExceptionHandlerServiceServiceImpl::logError)
                    .filter(callStatus -> callStatus.getStatus().equals(Status.SUCCESS))
                    .filter(callStatus -> Optional.ofNullable(callStatus.getCalledObject()).isPresent())
                    .map(CallStatus::getCalledObject)
                    .map(ExceptionHandlerServiceServiceImpl::handleCallStatus)
                    .flatMap(journeyMap -> journeyMap.entrySet().stream())
                    .filter(journey -> Optional.ofNullable(journey).isPresent())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (NullPointerException e) {
            log.error("Error during mapping callStatusList to journeyMap: ", e);
            return Collections.emptyMap();
        }
    }

    @Override
    public LinkedHashSet<TravelPoint> handleExceptionsTravelPoints(CallStatus<LinkedHashSet<TravelPointStatus>> linkedHashSetCallStatus) {
        logError(linkedHashSetCallStatus);
        if (Status.SUCCESS.equals(linkedHashSetCallStatus.getStatus())) {
            return Optional.ofNullable(linkedHashSetCallStatus.getCalledObject())
                    .map(travelPoints -> travelPoints
                            .stream()
                            .peek(ExceptionHandlerServiceServiceImpl::logErrorTravelPoint)
                            .map(TravelPointStatus::getTravelPoint)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toCollection(LinkedHashSet::new))
                    ).orElse(new LinkedHashSet<>());
        }
        return new LinkedHashSet<>();
    }

    @Override
    public TravelPoint handleExceptions(CallStatus<TravelPointStatus> travelPointCallStatus) {
        logError(travelPointCallStatus);
        if (Status.SUCCESS.equals(travelPointCallStatus.getStatus())) {
            return Optional.ofNullable(travelPointCallStatus.getCalledObject())
                    .map(travelPointStatus -> {
                        logErrorTravelPoint(travelPointStatus);
                        return travelPointStatus;
                    })
                    .flatMap(TravelPointStatus::getTravelPoint)
                    .orElse(new TravelPoint.TravelPointBuilder().build());
        }
        return new TravelPoint.TravelPointBuilder().build();
    }

    private static <T> void logError(CallStatus<T> callStatus) {
        if (Status.FAILED.equals(callStatus.getStatus())) {
            log.error("Error during Service Call.", callStatus.getException());
        }
    }
}