package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BbcConfiguration {

    @Value("${bbcKey}")
    private String authorization;
    @Value("${bbcHost}")
    private String bbcHost;
    @Value("${bbcPathVariable}")
    private String bbcPathVariable;
    @Value("${bbcJourneyPathVariable}")
    private String bbcJourneyPathVariable;
    @Value("${bbcApiVersion}")
    private String bbcApiVersion;
    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${bbcLanguage}")
    private String bbcLanguage;
    @Value("${bbcCurrency}")
    private String bbcCurrency;
    @Value("${bbcRadius}")
    private int bbcRadius;
    @Value("${bbcNumberOfPersons}")
    private int bbcNumberOfPersons;
    @Value("${bbcResultLength}")
    private int bbcResultLength;
    @Value("${bbcTimeIsDeparture}")
    private boolean bbcTimeIsDeparture;
    @Value("${bbcSortDirection}")
    private String bbcSortDirection;

    @Bean(name = "bbcApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setAuthorization(authorization);
        builder.setHost(bbcHost);
        builder.setPathVariable(bbcPathVariable);
        builder.setJourneyPathVariable(bbcJourneyPathVariable);
        builder.setApiVersion(bbcApiVersion);
        builder.setProtocol(accessProtocol);
        builder.setLanguage(bbcLanguage);
        builder.setCurrency(bbcCurrency);
        builder.setRadius(bbcRadius);
        builder.setNumberOfPersons(bbcNumberOfPersons);
        builder.setResultLength(bbcResultLength);
        builder.setTimeIsDeparture(bbcTimeIsDeparture);
        builder.setSortDirection(bbcSortDirection);
        return builder.build();
    }
}
