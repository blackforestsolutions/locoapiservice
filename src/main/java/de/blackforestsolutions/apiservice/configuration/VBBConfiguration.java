package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class VBBConfiguration {

    private final ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration;
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
    public VBBConfiguration(ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration) {
        this.hafasStandardConfiguration = hafasStandardConfiguration;
    }

    @Bean(name = "vbbApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        hafasStandardConfiguration.setHost(hafasVbbHost);
        hafasStandardConfiguration.setApiName(hafasVbbExtension);
        hafasStandardConfiguration.setMic(hafasVbbMic);
        hafasStandardConfiguration.setMac(hafasVbbMac);
        hafasStandardConfiguration.setClientId(hafasVbbClientId);
        hafasStandardConfiguration.setClientVersion(hafasVbbClientVersion);
        hafasStandardConfiguration.setClientName(hafasVbbClientName);
        hafasStandardConfiguration.setClientType(hafasVbbClientType);
        hafasStandardConfiguration.setAuthentificationType(hafasVbbAuthorizationType);
        hafasStandardConfiguration.setAuthorization(hafasVbbAuthorization);
        hafasStandardConfiguration.setAuthorizationKey(hafasVbbSalt);
        hafasStandardConfiguration.setHafasProductionValue(hafasVbbProductionValue);
        hafasStandardConfiguration.setWalkingSpeed(hafasVbbWalkingSpeed);
        hafasStandardConfiguration.setResultLengthAfterDepartureTime(hafasVbbShResultsAfterDepartureTime);
        hafasStandardConfiguration.setTimeIsDeparture(hafasVbbTimeIsDeparture);
        return hafasStandardConfiguration.build();
    }

}
