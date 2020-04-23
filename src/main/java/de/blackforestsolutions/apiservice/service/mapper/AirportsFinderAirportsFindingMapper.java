package de.blackforestsolutions.apiservice.service.mapper;


import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import de.blackforestsolutions.generatedcontent.airportsfinder.Location;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AirportsFinderAirportsFindingMapper {
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String CITY = "city";
    private static final String COUNTRY_CODE = "countryCode";


    public static List<AirportsFinding> map(List<LinkedHashMap<String, Object>> airportsFindindingLinkedHashMaps) {
        return airportsFindindingLinkedHashMaps
                .stream() // to stream
                .map(AirportsFinderAirportsFindingMapper::map) // for loop
                .collect(Collectors.toList()); // collects the data from the for loop and returns as List
    }


    private static AirportsFinding map(LinkedHashMap<String, Object> airportFindingsLinkedHashmap) {
        AirportsFinding airportsFinding = new AirportsFinding();
        Optional.ofNullable(airportFindingsLinkedHashmap.get(CODE)).ifPresent(code -> airportsFinding.setCode((String) airportFindingsLinkedHashmap.get(CODE)));
        Optional.ofNullable(airportFindingsLinkedHashmap.get(NAME)).ifPresent(name -> airportsFinding.setName((String) airportFindingsLinkedHashmap.get(NAME)));
        Optional.ofNullable(airportFindingsLinkedHashmap.get(CITY)).ifPresent(city -> airportsFinding.setCity((String) airportFindingsLinkedHashmap.get(CITY)));
        Optional.ofNullable(airportFindingsLinkedHashmap.get(COUNTRY_CODE)).ifPresent(code -> airportsFinding.setCountryCode((String) airportFindingsLinkedHashmap.get(COUNTRY_CODE)));
        setAirportFindingsGpsCoordinatesWith(airportFindingsLinkedHashmap, airportsFinding);
        return airportsFinding;
    }

    @SuppressWarnings("unchecked")
    private static void setAirportFindingsGpsCoordinatesWith(LinkedHashMap<String, Object> airportFindingsLinkedHashmap, AirportsFinding airportsFinding) {
        Location loc = new Location();
        if (airportFindingsLinkedHashmap.containsKey(LOCATION)) {
            loc.setLongitude(extractFrom("longitude", (LinkedHashMap<String, Double>) airportFindingsLinkedHashmap.get(LOCATION)));
            loc.setLatitude(extractFrom("latitude", (LinkedHashMap<String, Double>) airportFindingsLinkedHashmap.get(LOCATION)));
            airportsFinding.setLocation(loc);
        }
    }

    private static Double extractFrom(String key, LinkedHashMap<String, Double> coordinates) {
        return coordinates.get(key);
    }
}
