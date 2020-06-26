package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.LinkedHashSet;

public interface AirportsFinderMapperService {
    LinkedHashSet<CallStatus<TravelPoint>> map(String jsonString);
}
