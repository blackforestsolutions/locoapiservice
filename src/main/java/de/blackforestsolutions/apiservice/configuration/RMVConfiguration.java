package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@SpringBootConfiguration
public class RMVConfiguration {

    @Value("${rmvAuthorization}")
    private String authorization;
    @Value("${rmvHost}")
    private String host;
    @Value("${rmvpathlocation}")
    private String rmvPathLocation;
    @Value("${rmvpathtrip}")
    private String rmvPathTrip;
    @Value("${accessProtocol}")
    private String accesProtocol;

    @Bean(name = "rMVApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accesProtocol);
        builder.setHost(host);
        builder.setPort(0);
        builder.setPathVariable(rmvPathLocation);
        builder.setLocationPath(rmvPathLocation);
        builder.setGermanRailJourneyDeatilsPath(rmvPathTrip);
        builder.setAuthorizationKey(HttpHeaders.AUTHORIZATION);
        builder.setAuthorization(authorization);
        return builder.build();
    }
}
