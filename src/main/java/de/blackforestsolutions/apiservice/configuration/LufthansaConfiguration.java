package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.apiservice.service.communicationservice.LufthansaApiService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.lufthansa.LufthansaAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootConfiguration
@EnableScheduling
@EnableAsync
public class LufthansaConfiguration {

    private static final int MILLISECONDS = 1000;

    @Autowired
    private LufthansaApiService lufthansaApiService;

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

    private String authorization;

    @Async
    @Scheduled(fixedRate = 129600 * MILLISECONDS - 43200)
    void getBearerTokenForLufthansa() {
        CallStatus callStatus = lufthansaApiService.getLufthansaAuthorization(lufthansaApiTokenAndUrlInformation);
        if (callStatus.getStatus().equals(Status.SUCCESS)) {
            LufthansaAuthorization lufthansaAuthorization = (LufthansaAuthorization) callStatus.getCalledObject();
            authorization = lufthansaAuthorization.getAccessToken();
            new LufthansaConfiguration().apiTokenAndUrlInformation();
        }
        luf.set
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
        builder.setAuthorization(authorization);
        builder.setClientId(lufthansaClientId);
        builder.setClientSecret(lufthansaClientSecret);
        builder.setClientType(lufthansaClientType);
        return builder.build();
    }
}
