package de.blackforestsolutions.apiservice.service.mapper;


import de.blackforestsolutions.generatedcontent.airportsfinder.AirportsFinding;
import de.blackforestsolutions.generatedcontent.airportsfinder.Location;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AirportsFindingMapper {
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String CITY = "city";
    private static final String COUNTRY_CODE = "countryCode";


    public static List<AirportsFinding> map(List<LinkedHashMap<String, Object>> airportsFindindingLinkedHashMaps) {
        return airportsFindindingLinkedHashMaps
                .stream() // to stream
                .map(AirportsFindingMapper::map) // for loop
                .collect(Collectors.toList()); // collects the data from the for loop and returns as List
    }


    private static AirportsFinding map(LinkedHashMap<String, Object> airportFindingsLinkedHashmap) {
        AirportsFinding airportsFinding = new AirportsFinding();
        setAirportFindingsCodeWith(airportFindingsLinkedHashmap, airportsFinding);
        setAirportFindingsNameWith(airportFindingsLinkedHashmap, airportsFinding);
        setAirportFindingsCityWith(airportFindingsLinkedHashmap, airportsFinding);
        setAirportFindingsCountryCodeWith(airportFindingsLinkedHashmap, airportsFinding);
        setAirportFindingsGpsCoordinatesWith(airportFindingsLinkedHashmap, airportsFinding);
        return airportsFinding;
    }

    // error handling here - what if something can't be mapped to AirportsFinding
    private static void setAirportFindingsCodeWith(LinkedHashMap<String, Object> airportFindingsLinkedHashmap, AirportsFinding airportsFinding) {
        if (airportFindingsLinkedHashmap.get(CODE) != null) {
            airportsFinding.setCode((String) airportFindingsLinkedHashmap.get(CODE));
        }
    }

    private static void setAirportFindingsNameWith(LinkedHashMap<String, Object> airportFindingsLinkedHashmap, AirportsFinding airportsFinding) {
        if (airportFindingsLinkedHashmap.get(NAME) != null) {
            airportsFinding.setName((String) airportFindingsLinkedHashmap.get(NAME));
        }
    }

    private static void setAirportFindingsCityWith(LinkedHashMap<String, Object> airportFindingsLinkedHashmap, AirportsFinding airportsFinding) {
        if (airportFindingsLinkedHashmap.get(CITY) != null) {
            airportsFinding.setCity((String) airportFindingsLinkedHashmap.get(CITY));
        }
    }

    private static void setAirportFindingsCountryCodeWith(LinkedHashMap<String, Object> airportFindingsLinkedHashmap, AirportsFinding airportsFinding) {
        if (airportFindingsLinkedHashmap.get(COUNTRY_CODE) != null) {
            airportsFinding.setCountryCode((String) airportFindingsLinkedHashmap.get(COUNTRY_CODE));
        }
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
