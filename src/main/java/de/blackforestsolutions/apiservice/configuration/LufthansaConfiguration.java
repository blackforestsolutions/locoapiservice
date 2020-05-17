package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootConfiguration
@EnableScheduling
public class LufthansaConfiguration {

    @Value("${xOriginatingIP}")
    private String xOriginationIp;
    @Value("${lufthansaClientId}")
    private String lufthansaClientId;
    @Value("${lufthansaClientSecret}")
    private String lufthansaClientSecret;
    @Value("${lufthansaClientType}")
    private String lufthansaClientType;
    @Value("${lufthansaHost}")
    private String lufthansaHost;
    @Value("${lufthansaAuthorizationPathVariable}")
    private String lufthansaAuthorizationPathVariable;
    @Value("${lufthansaJourneyPathVariable}")
    private String lufthansaJourneyPathVariable;
    @Value("${lufthansaVersion}")
    private String lufthansaApiVersion;
    @Value("${accessProtocol}")
    private String accessProtocol;

    @Bean(name = "lufthansaApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accessProtocol);
        builder.setHost(lufthansaHost);
        builder.setPort(0);
        builder.setApiVersion(lufthansaApiVersion);
        builder.setPathVariable(lufthansaAuthorizationPathVariable);
        builder.setJourneyPathVariable(lufthansaJourneyPathVariable);
        builder.setXOriginationIpKey(de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration.X_ORIGINATING_IP);
        builder.setXOriginationIp(xOriginationIp);
        builder.setClientId(lufthansaClientId);
        builder.setClientSecret(lufthansaClientSecret);
        builder.setClientType(lufthansaClientType);
        return builder.build();
    }
}
