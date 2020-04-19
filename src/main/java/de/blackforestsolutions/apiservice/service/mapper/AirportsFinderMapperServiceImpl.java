package de.blackforestsolutions.apiservice.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AirportsFinderMapperServiceImpl implements AirportsFinderMapperService {
    private final Map<String, TravelPoint.TravelPointBuilder> airports;

    @Autowired
    public AirportsFinderMapperServiceImpl(Map<String, TravelPoint.TravelPointBuilder> airports) {
        this.airports = airports;
    }

    @Override
    public Set<TravelPoint> map(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        List<LinkedHashMap<String, Object>> airportsFindingList = null;
        try {
            airportsFindingList = mapper.readValue(jsonString, List.class);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping json to Object: {}", jsonString, e);
        }
        return mapAirportFindingsToTravelPointList(airportsFindingList);
    }


    private Set<TravelPoint> mapAirportFindingsToTravelPointList(List<LinkedHashMap<String, Object>> airportsFindingList) {
        // to check if the station has an airportcode, if not it won't be mapped
        Set<TravelPoint> nearestAirportTravelPointList = new HashSet<>();
        List<AirportsFinding> airportsFindings = AirportsFindingMapper.map(airportsFindingList);
        for (AirportsFinding entry : airportsFindings) {
            nearestAirportTravelPointList.add(buildTravelPointWith(entry));
        }
        return nearestAirportTravelPointList;
    }

    private TravelPoint buildTravelPointWith(AirportsFinding airportsFinding) {
        String airportCode = airportsFinding.getCode();
        if (airports.get(airportCode) != null) {
            TravelPoint.TravelPointBuilder travelPoint = airports.get(airportCode);
            return travelPoint.build();
        } /*else if (airportCode != null) {
            //return buildTravelPointManually(airportsFinding);

        }*/ else {
            return null; // todo error Handling
        }
    }
}
    /*private TravelPoint buildTravelPointManually(AirportsFinding airportsFinding) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setAirportId(airportsFinding.getCode());
        travelPoint.setStationName(airportsFinding.getName());
        travelPoint.setCity(airportsFinding.getCity());
        travelPoint.setCountry(new Locale(airportsFinding.getCountryCode()));
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(airportsFinding.getLocation().getLatitude(), airportsFinding.getLocation().getLongitude()).build());
        return travelPoint.build();
    }*/


// create TravelPoints
   /* private Set<TravelPoint> mapAirportFindingsToTravelPointList(List<LinkedHashMap<String, String>> airportsFindingList) {
        // to check if the station has an airportcode, if not it won't be mapped
        List<AirportsFinding> airportsFindings = AirportsFindingMapper.map(airportsFindingList);
        return airportsFindings
                .stream() // to stream
                .map(AirportsFinderMapperServiceImpl::buildTravelPointWith) // for loop
                .collect(Collectors.toSet()); // collects the data from the for loop and returns as List
    }*/
