package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import static de.blackforestsolutions.apiservice.configuration.AdditionalHttpHeadersConfiguration.BA_CLIENT_KEY;

@SpringBootConfiguration
public class BritishAirwaysConfiguration {

    private static final int CONFIGURED_BASE_PORT = 0;
    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${baClientKey}")
    private String baClientKey;
    @Value("${baHost}")
    private String baHost;
    @Value("${baVersion}")
    private String baVersion;
    @Value("${baVariable}")
    private String baVariable;

    @Bean(name = "britishAirwaysApiTokenAndUrlInformation") //
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setApiVersion(baVersion);
        builder.setProtocol(accessProtocol);
        builder.setHost(baHost);
        builder.setPort(CONFIGURED_BASE_PORT);
        builder.setPathVariable(baVariable);
        builder.setAuthorizationKey(BA_CLIENT_KEY);
        builder.setAuthorization(baClientKey);
        return builder.build();
    }
}

