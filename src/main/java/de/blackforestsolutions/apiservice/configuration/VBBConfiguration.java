package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class VBBConfiguration {

    private final ApiTokenAndUrlInformation hafasStandardConfiguration;

    @Value("${hafasVbbExtension}")
    private String hafasVbbExtension;
    @Value("${hafasVbbHost}")
    private String hafasVbbHost;
    @Value("${hafasVbbMic}")
    private String hafasVbbMic;
    @Value("${hafasVbbMac}")
    private String hafasVbbMac;
    @Value("${hafasVbbClientId}")
    private String hafasVbbClientId;
    @Value("${hafasVbbClientVersion}")
    private String hafasVbbClientVersion;
    @Value("${hafasVbbClientName}")
    private String hafasVbbClientName;
    @Value("${hafasVbbClientType}")
    private String hafasVbbClientType;
    @Value("${hafasVbbAuthorizationType}")
    private String hafasVbbAuthorizationType;
    @Value("${hafasVbbAuthorization}")
    private String hafasVbbAuthorization;
    @Value("${hafasVbbSalt}")
    private String hafasVbbSalt;
    @Value("${hafasVbbProductionValue}")
    private String hafasVbbProductionValue;
    @Value("${hafasVbbWalkingSpeed}")
    private String hafasVbbWalkingSpeed;
    @Value("${hafasVbbShResultsAfterDepartureTime}")
    private int hafasVbbShResultsAfterDepartureTime;
    @Value("${hafasVbbTimeIsDeparture}")
    private boolean hafasVbbTimeIsDeparture;

    @Autowired
    public VBBConfiguration(ApiTokenAndUrlInformation hafasStandardConfiguration) {
        this.hafasStandardConfiguration = hafasStandardConfiguration;
    }

    @Bean(name = "vbbApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hafasStandardConfiguration);
        builder.setHost(hafasVbbHost);
        builder.setApiName(hafasVbbExtension);
        builder.setMic(hafasVbbMic);
        builder.setMac(hafasVbbMac);
        builder.setClientId(hafasVbbClientId);
        builder.setClientVersion(hafasVbbClientVersion);
        builder.setClientName(hafasVbbClientName);
        builder.setClientType(hafasVbbClientType);
        builder.setAuthentificationType(hafasVbbAuthorizationType);
        builder.setAuthorization(hafasVbbAuthorization);
        builder.setAuthorizationKey(hafasVbbSalt);
        builder.setHafasProductionValue(hafasVbbProductionValue);
        builder.setWalkingSpeed(hafasVbbWalkingSpeed);
        builder.setResultLengthAfterDepartureTime(hafasVbbShResultsAfterDepartureTime);
        builder.setTimeIsDeparture(hafasVbbTimeIsDeparture);
        return builder.build();
    }

}
