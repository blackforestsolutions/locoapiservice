package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirportsFinderMapperServiceImpl implements AirportsFinderMapperService {

    private final Map<String, TravelPoint.TravelPointBuilder> airports;

    @Autowired
    public AirportsFinderMapperServiceImpl(Map<String, TravelPoint.TravelPointBuilder> airports) {
        this.airports = airports;
    }

    @Override
    public LinkedHashSet<CallStatus<TravelPoint>> map(String jsonString) {
        return mapAirportFindingsToTravelPointList(retrieveAirportsFinding(jsonString));
    }

    private CallStatus<List<AirportsFinding>> retrieveAirportsFinding(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return new CallStatus<>(mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, AirportsFinding.class)), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping json to Object: {}", jsonString, e);
            return new CallStatus<>(null, Status.FAILED, e);
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedHashSet<CallStatus<TravelPoint>> mapAirportFindingsToTravelPointList(CallStatus<List<AirportsFinding>> airportsFindingStatus) {
        if (airportsFindingStatus.getCalledObject() != null) {
            List<AirportsFinding> airportsFindingList = airportsFindingStatus.getCalledObject();
            return airportsFindingList
                    .stream()
                    .map(this::buildTravelPointWith)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return null;
    }

    private CallStatus<TravelPoint> buildTravelPointWith(AirportsFinding airportsFinding) {
        String airportCode = airportsFinding.getCode();
        if (airports.get(airportCode) != null) {
            TravelPoint.TravelPointBuilder travelPoint = airports.get(airportCode);
            return new CallStatus<>(travelPoint.build(), Status.SUCCESS, null);
        } else {
            return new CallStatus<>(null, Status.FAILED, checkForKindOfException(airportCode));
        }
    }

    private Exception checkForKindOfException(String airportCode) {
        Exception e;
        if (airportCode != null) {
            e = new Exception("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat");
        } else {
            e = new Exception("The provided AirportFinding object is not mapped because the airport code appears to be null");
        }
        return e;
    }

}
