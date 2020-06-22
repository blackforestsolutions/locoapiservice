package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface LufthansaMapperService {

    Map<UUID, JourneyStatus> map(String jsonString);

    CallStatus mapToAuthorization(String jsonString);
}
