package de.blackforestsolutions.apiservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

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

    private Point first = new Point(upperBoxLat, upperBoxLon);
    private Point second = new Point(lowerBoxLat, lowerBoxLon);

    //Config kommt in die entsprechende API Config -> RMV
    @Bean
    public Box box() {
        return new Box(first, second);
    }
    //todo die Bean gibt null weiter.
    //todo davon abgesehen: Beans Autowiren? - nützlich? wieso gefährlich?
    //this.first = first;
    //this.second = second;
}
