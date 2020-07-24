package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.datamodel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class ExceptionHandlerServiceServiceImpl implements ExceptionHandlerService {

    private static void logErrorTravelPoint(TravelPointStatus travelPointStatus) {
        if (travelPointStatus.getProblem().isPresent()) {
            log.error("Mapping Error for travel point {}", travelPointStatus.getProblem().get().getExceptions());
        }
    }

    private static Map<UUID, Journey> handleCallStatusList(List<CallStatus<Map<UUID, JourneyStatus>>> callStatusList) {
        callStatusList.forEach(ExceptionHandlerServiceServiceImpl::logError);
        List<Map<UUID, Journey>> result = callStatusList.stream()
                .map(ExceptionHandlerServiceServiceImpl::handleCallStatus)
                .collect(Collectors.toList());
        return reduce(result);
    }

    private static Map<UUID, Journey> reduce(List<Map<UUID, Journey>> mapList) {
        Map<UUID, Journey> journeyMap = new HashMap<>();
        mapList.forEach(journeyMap::putAll);
        return journeyMap;
    }

    private static void logError(CallStatus<Map<UUID, JourneyStatus>> callStatus) {
        if (Status.FAILED.equals(callStatus.getStatus())) {
            log.error("A call failed due to", callStatus.getException());
        }
    }

    private static Map<UUID, Journey> handleCallStatus(CallStatus<Map<UUID, JourneyStatus>> callStatus) {
        if (callStatus.getCalledObject() != null) {
            callStatus.getCalledObject().values().forEach(ExceptionHandlerServiceServiceImpl::logError);
            return callStatus.getCalledObject().values().stream()
                    .filter(journeyStatus -> journeyStatus.getJourney().isPresent())
                    .map(JourneyStatus::getJourney)
                    .map(Optional::get)
                    .collect(toMap(Journey::getId, journey -> journey));
        }
        return new HashMap<>();
    }

    private static void logError(JourneyStatus journeyStatus) {
        if (journeyStatus.getProblem().isPresent()) {
            log.error("A mapping failed due to {}", journeyStatus.getProblem().get().getExceptions());
        }
    }

    @Override
    public Map<UUID, Journey> handleExceptions(List<CallStatus<Map<UUID, JourneyStatus>>> callStatusList) {
        return handleCallStatusList(callStatusList);
    }

    @Override
    public LinkedHashSet<TravelPoint> handleExceptionsTravelPoints(CallStatus<LinkedHashSet<TravelPointStatus>> linkedHashSetCallStatus) {
        logErrorCallStatus(linkedHashSetCallStatus);
        if (Status.SUCCESS.equals(linkedHashSetCallStatus.getStatus())) {
            linkedHashSetCallStatus.getCalledObject()
                    .forEach(ExceptionHandlerServiceServiceImpl::logErrorTravelPoint);
            return linkedHashSetCallStatus.getCalledObject().stream()
                    .map(TravelPointStatus::getTravelPoint)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toCollection(LinkedHashSet::new));
        }
        return null;
    }

    private void logErrorCallStatus(CallStatus<LinkedHashSet<TravelPointStatus>> linkedHashSetCallStatus) {
        if (Status.FAILED.equals(linkedHashSetCallStatus.getStatus())) {
            log.error("Error during Service Call.", linkedHashSetCallStatus.getException());
        }
    }

    @Override
    public Coordinates handleExceptions(CallStatus<Coordinates> coordinatesCallStatus) {
        logErrorCoordinate(coordinatesCallStatus);
        if (Status.SUCCESS.equals(coordinatesCallStatus.getStatus())) {
            return coordinatesCallStatus.getCalledObject();
        }
        return null;
    }

    private void logErrorCoordinate(CallStatus<Coordinates> coordinatesCallStatus) {
        if (Status.FAILED.equals(coordinatesCallStatus.getStatus())) {
            log.error("Error during Service Call.", coordinatesCallStatus.getException());
        }
    }
}