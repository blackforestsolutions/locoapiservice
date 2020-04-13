package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.TravelPoint;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@SpringBootConfiguration
/*
  This configuration contains a parsing logic which is parsing a csv file with
  all known airports we need this and can use this to verify and check airline api data.
 */
public class AirportConfiguration {

    private static final int COLUMN_SIZE_FOURTEEN = 14;
    private static final int COLUMN_SIZE_FIFTEEN = 15;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final String AIRPORTS_FILE_PATH = "airports/airports.dat";

    @Bean(name = "airports")
    public Map<String, TravelPoint.TravelPointBuilder> airports() throws IOException {
        Map<String, TravelPoint.TravelPointBuilder> airports = new LinkedHashMap<>();
        ClassPathResource resource = new ClassPathResource(AIRPORTS_FILE_PATH);
        List<String> resourceLines = IOUtils.readLines(resource.getInputStream(), Charset.defaultCharset());
        fillAirportFrom(resourceLines, airports);
        return airports;
    }

    private void fillAirportFrom(List<String> lines, Map<String, TravelPoint.TravelPointBuilder> airports) {
        lines.forEach(line -> {
            List<String> columnEntries = Arrays.asList(line.split(","));
            if (columnEntries.size() == COLUMN_SIZE_FOURTEEN) {
                int lineCorrection = 0;
                fillTravelPointAttribute(columnEntries, airports, lineCorrection);
            }
            if (columnEntries.size() == COLUMN_SIZE_FIFTEEN) {
                int lineCorrection = 1;
                fillTravelPointAttribute(columnEntries, airports, lineCorrection);
            }
        });
    }

    private void fillTravelPointAttribute(List<String> columnEntries, Map<String, TravelPoint.TravelPointBuilder> airports, int lineCorrection) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setAirportName(columnEntries.get(ONE + lineCorrection).replace("\"", ""));
        travelPoint.setCity(columnEntries.get(TWO + lineCorrection).replace("\"", ""));
        travelPoint.setAirportId(columnEntries.get(FOUR + lineCorrection).replace("\"", ""));
        travelPoint.setCountry(new Locale("", columnEntries.get(THREE + lineCorrection).replace("\"", "")));
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(
                        Double.parseDouble(columnEntries.get(SIX + lineCorrection).replace("\"", "")),
                        Double.parseDouble(columnEntries.get(SEVEN + lineCorrection))
                ).build()
        );
        airports.put(columnEntries.get(FOUR + lineCorrection).replace("\"", ""), travelPoint);
    }
}
