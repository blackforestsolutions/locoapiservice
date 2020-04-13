package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class OSMConfiguration {

    @Value("${accessProtocol}")
    private String accesProtocol;
    @Value("${osmHost}")
    private String osmHost;
    @Value("${osmOutputFormat}")
    private String osmOutputFormat;
    @Value("${osmResultLength}")
    private int osmResultLength;

    @Bean(name = "osmApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accesProtocol);
        builder.setHost(osmHost);
        builder.setPort(0);
        builder.setOutputFormat(osmOutputFormat);
        builder.setResultLength(osmResultLength);
        return builder.build();
    }
}
