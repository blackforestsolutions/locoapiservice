package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.datamodel.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExceptionHandlerService {
    Map<UUID, Journey> handleExceptions(List<CallStatus<Map<UUID, JourneyStatus>>> callStatusList);

    LinkedHashSet<TravelPoint> handleExceptionsTravelPoints(CallStatus<LinkedHashSet<TravelPointStatus>> linkedHashSetCallStatus);

    Coordinates handleExceptions(CallStatus<Coordinates> coordinatesCallStatus);
}
