package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfiguration {

    private final ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration;
    @Value("${hafasDbChecksum}")
    private String hafasDbChecksum;
    @Value("${hafasDbHost}")
    private String hafasDbHost;
    @Value("${hafasDbExtension}")
    private String hafasDbExtension;
    @Value("${hafasDbRtMode}")
    private String hafasDbRtMode;
    @Value("${hafasDbClientId}")
    private String hafasDbClientId;
    @Value("${hafasDbClientVersion}")
    private String hafasDbClientVersion;
    @Value("${hafasDbClientName}")
    private String hafasDbClientName;
    @Value("${hafasDbClientType}")
    private String hafasDbClientType;
    @Value("${hafasDbAuthorizationType}")
    private String hafasDbAuthorizationType;
    @Value("${hafasDbAuthorization}")
    private String hafasDbAuthorization;
    @Value("${hafasDbSalt}")
    private String hafasDbSalt;
    @Value("${hafasDbProductionValue}")
    private String hafasDbProductionValue;
    @Value("${hafasDbResultsAfterDepartureTime}")
    private int hafasDbResultsAfterDepartureTime;
    @Value("${hafasDbTimeIsDeparture}")
    private boolean hafasDbTimeIsDeparture;
    @Value("${hafasDbAllowReduced}")
    private boolean hafasDbAllowReduced;

    @Autowired
    public DBConfiguration(ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration) {
        this.hafasStandardConfiguration = hafasStandardConfiguration;
    }

    @Bean(name = "dbApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        hafasStandardConfiguration.setHost(hafasDbHost);
        hafasStandardConfiguration.setApiName(hafasDbExtension);
        hafasStandardConfiguration.setChecksum(hafasDbChecksum);
        hafasStandardConfiguration.setHafasRtMode(hafasDbRtMode);
        hafasStandardConfiguration.setCliendId(hafasDbClientId);
        hafasStandardConfiguration.setClientVersion(hafasDbClientVersion);
        hafasStandardConfiguration.setClientName(hafasDbClientName);
        hafasStandardConfiguration.setClientType(hafasDbClientType);
        hafasStandardConfiguration.setAuthentificationType(hafasDbAuthorizationType);
        hafasStandardConfiguration.setAuthorization(hafasDbAuthorization);
        hafasStandardConfiguration.setAuthorizationKey(hafasDbSalt);
        hafasStandardConfiguration.setHafasProductionValue(hafasDbProductionValue);
        hafasStandardConfiguration.setResultLengthAfterDepartureTime(hafasDbResultsAfterDepartureTime);
        hafasStandardConfiguration.setTimeIsDeparture(hafasDbTimeIsDeparture);
        hafasStandardConfiguration.setAllowReducedPrice(hafasDbAllowReduced);
        return hafasStandardConfiguration.build();
    }
}
