package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SpringBootConfiguration
@EnableScheduling
public class LufthansaConfiguration {

    private static final int EXPIRATION_TIME_IN_MILLISECONDS = 86400000;
    private static final int MILLISECONDS = 1000;
    private static final double SECURITY_EXPIRATION_TIME = 2 / 3d;

    private final LufthansaApiService lufthansaApiService;

    private final ApplicationContext applicationContext;

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

    @Autowired
    public LufthansaConfiguration(LufthansaApiService lufthansaApiService, ApplicationContext applicationContext) {
        this.lufthansaApiService = lufthansaApiService;
        this.applicationContext = applicationContext;
    }

    @Scheduled(fixedRate = EXPIRATION_TIME_IN_MILLISECONDS)
    public void getBearerTokenForLufthansa() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = applicationContext.getBean("lufthansaApiTokenAndUrlInformation", ApiTokenAndUrlInformation.class);
        CallStatus callStatus = lufthansaApiService.getLufthansaAuthorization(apiTokenAndUrlInformation);
        if (callStatus.getStatus().equals(Status.SUCCESS)) {
            LufthansaAuthorization lufthansaAuthorization = (LufthansaAuthorization) callStatus.getCalledObject();
            String authorization = lufthansaAuthorization.getAccessToken();
            apiTokenAndUrlInformation.setAuthorization("Bearer ".concat(authorization));
            log.info("Luthansa Api Token was updated");
            if (lufthansaAuthorization.getExpiresIn() * MILLISECONDS * SECURITY_EXPIRATION_TIME != EXPIRATION_TIME_IN_MILLISECONDS) {
                log.warn("Expiration Time for lufthansa token has changed!");
            }
        } else {
            log.warn("Bearer token could no be updated!");
        }
    }

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
