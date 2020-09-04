package de.blackforestsolutions.apiservice.service.exceptionhandling;

import de.blackforestsolutions.datamodel.*;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExceptionHandlerService {

    Map<UUID, Journey> handleExceptions(List<CallStatus<Map<UUID, JourneyStatus>>> callStatusList);

    LinkedHashSet<TravelPoint> handleExceptionsTravelPoints(CallStatus<LinkedHashSet<TravelPointStatus>> linkedHashSetCallStatus);

    TravelPoint handleExceptionsOld(CallStatus<TravelPointStatus> travelPointCallStatus);

    <T> Mono<T> handleExceptions(Throwable throwable);

    <T> Mono<T> handleExceptions(CallStatus<T> callStatus);
}
