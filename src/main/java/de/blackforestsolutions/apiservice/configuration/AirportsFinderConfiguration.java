package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AirportsFinderConfiguration {

    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${xRapidapiKeyValue}")
    private String xRapidapiKeyValue;
    @Value("${airportsFinderHost}")
    private String airportsFinderHost;
    @Value("${airportsFinderVariable}")
    private String airportsFinderVariable;
    @Value("${xRapidapiKey}")
    private String xRapidapiKey;


    @Bean(name = "airportsFinderApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accessProtocol);
        builder.setHost(airportsFinderHost);
        builder.setPathVariable(airportsFinderVariable);
        builder.setAuthorization(xRapidapiKeyValue);
        builder.setAuthorizationKey(xRapidapiKey);
        return builder.build();
    }
}
