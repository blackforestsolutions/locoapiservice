package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface SearchChMapperService {
    Map<String, TravelPoint> getTravelPointFrom(String jsonString) throws IOException;

    Map<UUID, JourneyStatus> getJourneysFrom(String jsonString);
}
