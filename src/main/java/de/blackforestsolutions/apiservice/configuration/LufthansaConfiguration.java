package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@SpringBootConfiguration
public class LufthansaConfiguration {

    @Value("${xOriginatingIP}")
    private String xOriginationIp;
    @Value("${authorization}")
    private String authorization;
    @Value("${lufthansaHost}")
    private String lufthansaHost;
    @Value("${lufthansaPathVariable}")
    private String lufthansaPathVariable;
    @Value("${lufthansaVersion}")
    private String lufthansaApiVersion;
    @Value("${accessProtocol}")
    private String accesProtocol;

    @Bean(name = "lufthansaApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accesProtocol);
        builder.setHost(lufthansaHost);
        builder.setPort(0);
        builder.setApiVersion(lufthansaApiVersion);
        builder.setPathVariable(lufthansaPathVariable);
        builder.setXOriginationIp(xOriginationIp);
        builder.setAuthorizationKey(HttpHeaders.AUTHORIZATION);
        builder.setAuthorization(authorization);
        return builder.build();
    }
}
