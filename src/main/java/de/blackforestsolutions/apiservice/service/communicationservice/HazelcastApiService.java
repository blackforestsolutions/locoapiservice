package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;

import java.util.Map;
import java.util.UUID;

public interface HazelcastApiService {

    String readFromHazelcast(String key, ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    void writeAllToHazelcast(Map<UUID, Journey> journeys, ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    String writeToHazelcast(String key, String value, ApiTokenAndUrlInformation apiTokenAndUrlInformation);

    Map<String, String> readAllFromHazelcast(ApiTokenAndUrlInformation apiTokenAndUrlInformation);
}
