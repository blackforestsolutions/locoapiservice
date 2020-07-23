package de.blackforestsolutions.apiservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BoxServiceConfiguration {


    @Value("${coord1lat}")
    private double upperBoxLat;
    @Value("${coord1lon}")
    private double upperBoxLon;
    @Value("${coord2lat}")
    private double lowerBoxLat;
    @Value("${coord2lon}")
    private double lowerBoxLon;

    @Bean(name = "upperBoxLat")
    public double upperBoxLat() {
        return upperBoxLat;
    }

    @Bean(name = "upperBoxLat")
    public double upperBoxLon() {
        return upperBoxLon;
    }

    @Bean(name = "upperBoxLat")
    public double lowerBoxLat() {
        return lowerBoxLat;
    }

    @Bean(name = "upperBoxLat")
    public double lowerBoxLon() {
        return lowerBoxLon;
    }
}
