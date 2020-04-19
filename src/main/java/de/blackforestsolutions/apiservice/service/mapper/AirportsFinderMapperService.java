package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.TravelPoint;

import java.util.Set;

public interface AirportsFinderMapperService {
    Set<TravelPoint> map(String jsonString);
}
