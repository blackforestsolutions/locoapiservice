package de.blackforestsolutions.apiservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HazelCallService;
import de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
public class HazelcastApiServiceImpl extends HttpCallBuilder implements HazelcastApiService {

    private final HazelCallService hazelCallService;
    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();

    @Autowired
    public HazelcastApiServiceImpl(HazelCallService hazelCallService) {
        this.hazelCallService = hazelCallService;
    }

    @Override
    public String readFromHazelcast(String key, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastPath());
        URL hazelcastBaseUrl = buildUrlWith(builder.build());
        return hazelCallService.getEntryByKey(hazelcastBaseUrl.toString(), key);
    }

    @Override
    public void writeAllToHazelcast(Map<UUID, Journey> journeys, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        journeys
                .values()
                .forEach(
                        journey -> write(journey, apiTokenAndUrlInformation));
    }

    private void write(Journey journey, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        String journeyString;
        try {
            journeyString = locoJsonMapper.map(journey);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping journey to string: ", e);
            journeyString = "Error during stringify:" + journey.getId().toString();
        }
        writeToHazelcast(journey.getId().toString(), journeyString, apiTokenAndUrlInformation);
    }

    @Override
    public String writeToHazelcast(String key, String value, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders headers = new HttpHeaders();
        setFormatToXmlFor(headers);
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastWritePath());
        URL hazelcastBaseUrl = buildUrlWith(builder.build());
        return hazelCallService.saveEntry(hazelcastBaseUrl.toString(), headers, key, value);
    }

    @Override
    public Map<String, String> readAllFromHazelcast(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(apiTokenAndUrlInformation);
        builder.setPath(apiTokenAndUrlInformation.getHazelcastReadAllPath());
        URL hazelcastBaseUrl = buildUrlWith(builder.build());
        return hazelCallService.getAllEntries(hazelcastBaseUrl.toString());
    }

}
