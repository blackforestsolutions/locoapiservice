package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BbcConfiguration {
    private static final int CONFIGURED_BASE_PORT = 0;
    @Value("${bbcKey}")
    private String authorization;
    @Value("${bbcHost}")
    private String bbcHost;
    @Value("${bbcPathVariable}")
    private String bbcPathVariable;
    @Value("${bbcApiVersion}")
    private String bbcApiVersion;
    @Value("${accessProtocol}")
    private String accesProtocol;

    @Bean(name = "bbcApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accesProtocol);
        builder.setHost(bbcHost);
        builder.setPort(CONFIGURED_BASE_PORT);
        builder.setApiVersion(bbcApiVersion);
        builder.setPathVariable(bbcPathVariable);
        builder.setAuthorizationKey(AdditionalHttpConfiguration.KEY);
        builder.setAuthorization(authorization);
        return builder.build();
    }
}
