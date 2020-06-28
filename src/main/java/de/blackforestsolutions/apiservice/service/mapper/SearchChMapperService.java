package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.JourneyStatus;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface SearchChMapperService {
    String getIdFromStation(String jsonString) throws IOException;

    Map<UUID, JourneyStatus> getJourneysFrom(String jsonString) throws JsonProcessingException;
}
