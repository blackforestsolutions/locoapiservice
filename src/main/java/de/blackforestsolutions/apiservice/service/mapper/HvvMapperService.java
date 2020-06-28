package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HvvMapperService {
    HvvStation getHvvStationFrom(String jsonString) throws JsonProcessingException;

    List<TravelPoint> getStationListFrom(String jsonBody) throws JsonProcessingException;

    Map<UUID, JourneyStatus> getJourneyMapFrom(String jsonBody) throws JsonProcessingException;
}
