package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.TravelPointStatus;

public interface OsmMapperService {
    TravelPointStatus mapOsmJsonToTravelPoint(String jsonString);
}
