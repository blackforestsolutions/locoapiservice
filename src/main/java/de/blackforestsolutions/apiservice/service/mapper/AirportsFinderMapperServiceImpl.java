package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelPointStatus;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.service.mapper.TravelPointStatusBuilder.createTravelPointStatusProblemWith;

@Slf4j
@Service
public class AirportsFinderMapperServiceImpl implements AirportsFinderMapperService {

    private final Map<String, TravelPoint.TravelPointBuilder> airports;

    @Autowired
    public AirportsFinderMapperServiceImpl(Map<String, TravelPoint.TravelPointBuilder> airports) {
        this.airports = airports;
    }

    @Override
    public LinkedHashSet<TravelPointStatus> map(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<AirportsFinding> airportsFindings = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, AirportsFinding.class));
        return mapAirportFindingsToTravelPointList(airportsFindings);
    }

    private LinkedHashSet<TravelPointStatus> mapAirportFindingsToTravelPointList(List<AirportsFinding> airports) {
        return airports
                .stream()
                .map(this::buildTravelPointWith)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private TravelPointStatus buildTravelPointWith(AirportsFinding airportsFinding) {
        String airportCode = airportsFinding.getCode();
        return Optional.ofNullable(airports.get(airportCode))
                .map(TravelPoint.TravelPointBuilder::build)
                .map(TravelPointStatusBuilder::createTravelPointStatusWith)
                .orElse(createTravelPointStatusProblemWith(
                        List.of(new Exception("The provided AirportFinding object is not mapped because the airport code is not provided in the airports.dat")),
                        Collections.emptyList()
                ));
    }

}
