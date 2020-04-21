package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;

import java.util.LinkedHashSet;

public interface AirportsFinderMapperService {
    LinkedHashSet<CallStatus> map(String jsonString);
}
