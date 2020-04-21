package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;

import java.util.Set;

public interface AirportsFinderMapperService {
    Set<CallStatus> map(String jsonString);
}
