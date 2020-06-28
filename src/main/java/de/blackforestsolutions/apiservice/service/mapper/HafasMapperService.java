package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;

import java.util.Map;
import java.util.UUID;

public interface HafasMapperService {

    Map<UUID, JourneyStatus> getJourneysFrom(String body, TravelProvider travelProvider, HafasPriceMapper priceMapper) throws JsonProcessingException;

    String getIdFrom(String resultBody) throws JsonProcessingException;
}
