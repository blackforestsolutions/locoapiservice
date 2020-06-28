package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.util.Map;
import java.util.UUID;

public interface BritishAirwaysMapperService {
    Map<UUID, JourneyStatus> map(String jsonString) throws JsonProcessingException;
}
