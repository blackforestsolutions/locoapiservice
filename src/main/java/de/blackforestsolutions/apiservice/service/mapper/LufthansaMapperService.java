package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;

public interface LufthansaMapperService {

    CallStatus map(String jsonString);

    CallStatus mapToAuthorization(String jsonString);
}
