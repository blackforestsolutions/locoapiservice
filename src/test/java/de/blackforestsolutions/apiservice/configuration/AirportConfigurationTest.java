package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.TravelPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

class AirportConfigurationTest {

    private final AirportConfiguration classUnderTest = new AirportConfiguration();

    @Test
    void test_get_all_configured_airports_without_error() throws IOException {

        Map<String, TravelPoint.TravelPointBuilder> airports = classUnderTest.airports();

        Assertions.assertThat(airports.get("LAX").build())
                .extracting(
                        TravelPoint::getStationName,
                        TravelPoint::getCity,
                        TravelPoint::getCountry)
                .containsExactly(
                        "Los Angeles International Airport",
                        "Los Angeles",
                        new Locale("", "UNITED STATES")
                );

        Assertions.assertThat(airports.get("TXL").build())
                .extracting(
                        TravelPoint::getStationName,
                        TravelPoint::getCity,
                        TravelPoint::getCountry)
                .containsExactly(
                        "Berlin-Tegel Airport",
                        "Berlin",
                        new Locale("", "GERMANY")
                );
    }

}
