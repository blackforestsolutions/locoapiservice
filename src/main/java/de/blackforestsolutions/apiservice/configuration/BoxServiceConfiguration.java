package de.blackforestsolutions.apiservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Box;

@SpringBootConfiguration
public class BoxServiceConfiguration {


    @Value("${coord1lat}")
    private double coord1lat;
    @Value("${coord1lon}")
    private double coord1lon;
    @Value("${coord2lat}")
    private double coord2lat;
    @Value("${coord2lon}")
    private double coord2lon;

    @Bean(name="boxObject")
    public Box boxe() {
        boxe().
    }
}
