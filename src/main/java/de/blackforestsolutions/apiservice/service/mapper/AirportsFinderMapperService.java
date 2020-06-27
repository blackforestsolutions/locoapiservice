package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.TravelPointStatus;

import java.util.LinkedHashSet;

public interface AirportsFinderMapperService {
    LinkedHashSet<TravelPointStatus> map(String jsonString) throws JsonProcessingException;
}
