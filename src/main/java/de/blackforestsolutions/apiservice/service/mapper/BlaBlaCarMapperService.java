package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import reactor.core.publisher.Flux;

public interface BlaBlaCarMapperService {

    Flux<CallStatus<Journey>> buildJourneysWith(Rides rides);
}
