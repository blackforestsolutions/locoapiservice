package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface RMVMapperService {
    CallStatus getIdFrom(String resultBody);

    Map<UUID, JourneyStatus> getJourneysFrom(String resultBody);
}
