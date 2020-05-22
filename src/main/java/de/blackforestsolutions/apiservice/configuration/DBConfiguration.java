package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class DBConfiguration {

    @Resource(name = "hafasStandardConfiguration")
    private ApiTokenAndUrlInformation hafasStandardConfiguration;

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

    @Bean(name = "dbApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hafasStandardConfiguration);
        builder.setHost(hafasDbHost);
        builder.setApiName(hafasDbExtension);
        builder.setChecksum(hafasDbChecksum);
        builder.setHafasRtMode(hafasDbRtMode);
        builder.setClientId(hafasDbClientId);
        builder.setClientVersion(hafasDbClientVersion);
        builder.setClientName(hafasDbClientName);
        builder.setClientType(hafasDbClientType);
        builder.setAuthentificationType(hafasDbAuthorizationType);
        builder.setAuthorization(hafasDbAuthorization);
        builder.setAuthorizationKey(hafasDbSalt);
        builder.setHafasProductionValue(hafasDbProductionValue);
        builder.setResultLengthAfterDepartureTime(hafasDbResultsAfterDepartureTime);
        builder.setTimeIsDeparture(hafasDbTimeIsDeparture);
        builder.setAllowReducedPrice(hafasDbAllowReduced);
        return builder.build();
    }
}
