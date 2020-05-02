package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.bbc.Rides;
import de.blackforestsolutions.generatedcontent.bbc.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinksterMapperServiceImpl {

    private final UuidService uuidService;

    @Autowired
    public FlinksterMapperServiceImpl(UuidService uuidService) { this.uuidService = uuidService; }

    private static CallStatus retrieveRidesStatusFrom(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return new CallStatus(mapper.readValue(jsonString, Rides.class), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error while processing json: ", e);
            return new CallStatus(null, Status.FAILED, e);
        }
    }


    public Map<UUID, JourneyStatus> map(String jsonString) {
        return mapTripsToJourneyList(retrieveRidesStatusFrom(jsonString));
    }
    private Map<UUID, JourneyStatus> mapTripsToJourneyList(CallStatus callStatus) {
        if (callStatus.getCalledObject() != null) {
            Rides ridesStatus = (Rides) callStatus.getCalledObject();
            return ridesStatus
                    .getTrips()
                    .stream()
                    .map(this::buildJourneyWith)
                    .collect(Collectors.toMap(Journey::getId, JourneyStatusBuilder::createJourneyStatusWith));
        } else {
            UUID errorUuid = UUID.randomUUID();
            return Map.of(errorUuid, JourneyStatusBuilder.createJourneyStatusProblemWith(callStatus.getException()));
        }

    }

    private Journey buildJourneyWith(Trip trip) {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(uuidService.createUUID());
        journey.setStartTime(buildDateFrom(trip));

        return null;
    }

    private static Date buildDateFrom(Trip trip) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateStringFixed = trip.getDepartureDate().replace("/", "-");
            return simpleDateFormat.parse(dateStringFixed);
        } catch (ParseException e) {
            log.error("Error while parsing Date and was replaced by new Date()", e);
            return new Date();
        }
    }

}
