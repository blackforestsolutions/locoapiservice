package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;

import java.util.Map;
import java.util.UUID;

public interface LufthansaMapperService {

    Map<UUID, JourneyStatus> map(String jsonString) throws JsonProcessingException;

    CallStatus<LufthansaAuthorization> mapToAuthorization(String jsonString);
}
