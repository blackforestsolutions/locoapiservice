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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public Set<CallStatus> map(String jsonString) {
        return mapAirportFindingsToTravelPointList(retrieveAirportsFinding(jsonString));
    }

    private CallStatus retrieveAirportsFinding(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return new CallStatus(mapper.readValue(jsonString, List.class), Status.SUCCESS, null);
        } catch (JsonProcessingException e) {
            log.error("Error during mapping json to Object: {}", jsonString, e);
            return new CallStatus(null, Status.FAILED, e);
        }
    }


    private Set<CallStatus> mapAirportFindingsToTravelPointList(CallStatus airportsFindingStatus) {
        if (airportsFindingStatus.getCalledObject() != null) {
            List<LinkedHashMap<String, Object>> airportsFindingList = (List<LinkedHashMap<String, Object>>) airportsFindingStatus.getCalledObject();
            List<AirportsFinding> airportsFindings = AirportsFindingMapper.map(airportsFindingList);
            return airportsFindings
                    .stream()
                    .map(airportsFinding -> buildTravelPointWith(airportsFinding))
                    .collect(Collectors.toSet());
        }
        return null;
    }

    private CallStatus buildTravelPointWith(AirportsFinding airportsFinding) {
        // to check if the station has an airport code, if not it won't be mapped and null will be returned
        // when an exception occurs it will be caught with a failed status in CallStatus object
        try {
            String airportCode = airportsFinding.getCode();
            if (airports.get(airportCode) != null) {
                TravelPoint.TravelPointBuilder travelPoint = airports.get(airportCode);
                return new CallStatus(travelPoint.build(), Status.SUCCESS, null);
            } else {
                return null;
            }

        } catch (NullPointerException e) {
            return new CallStatus(null, Status.FAILED, e);
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
