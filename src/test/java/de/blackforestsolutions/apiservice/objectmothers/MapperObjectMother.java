package de.blackforestsolutions.apiservice.objectmothers;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class MapperObjectMother {


    public static List<LinkedHashMap<String, Object>> getAirportsFinderListOfLinkedHashMaps() {
        List<LinkedHashMap<String, Object>> airportsFindindingLinkedHashMaps = new ArrayList<>();
        LinkedHashMap<String, Object> entryZero = new LinkedHashMap<>();
        LinkedHashMap<String, Object> entryOne = new LinkedHashMap<>();
        LinkedHashMap<String, Object> entryTwo = new LinkedHashMap<>();

        LinkedHashMap<String, Double> locationZero = new LinkedHashMap<>();
        locationZero.put("longitude", 6.381111);
        locationZero.put("latitude", 47.8168409);

        LinkedHashMap<String, Double> locationTwo = new LinkedHashMap<>();
        locationTwo.put("longitude", 5.4212699);
        locationTwo.put("latitude", 47.0436856);

        entryZero.put("code", "VTL");
        entryZero.put("name", "Vittel Champ De Course Airport");
        entryZero.put("city", "Luxeuil-les-Bains");
        entryZero.put("countryCode", "FR");
        entryZero.put("location", locationZero);

        entryOne.put("code", "QFB");
        entryTwo.put("code", "DLE");
        entryTwo.put("name", "Dole-Tavaux Airport");
        entryTwo.put("location", locationTwo);

        airportsFindindingLinkedHashMaps.add(entryZero);
        airportsFindindingLinkedHashMaps.add(entryOne);
        airportsFindindingLinkedHashMaps.add(entryTwo);
        return airportsFindindingLinkedHashMaps;
    }
}
