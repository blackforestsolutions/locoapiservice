package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BlaBlaCarConfiguration {

    @Value("${blaBlaCarKey}")
    private String authorization;
    @Value("${blaBlaCarHost}")
    private String blaBlaCarHost;
    @Value("${blaBlaCarPathVariable}")
    private String blaBlaCarPathVariable;
    @Value("${blaBlaCarJourneyPathVariable}")
    private String blaBlaCarJourneyPathVariable;
    @Value("${blaBlaCarApiVersion}")
    private String blaBlaCarApiVersion;
    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${blaBlaCarLanguage}")
    private String blaBlaCarLanguage;
    @Value("${blaBlaCarCurrency}")
    private String blaBlaCarCurrency;
    @Value("${blaBlaCarRadius}")
    private int blaBlaCarRadius;
    @Value("${blaBlaCarNumberOfPersons}")
    private int blaBlaCarNumberOfPersons;
    @Value("${blaBlaCarResultLength}")
    private int blaBlaCarResultLength;
    @Value("${blaBlaCarTimeIsDeparture}")
    private boolean blaBlaCarTimeIsDeparture;
    @Value("${blaBlaCarSortDirection}")
    private String blaBlaCarSortDirection;
    @Value("${blaBlaCarCountry}")
    private String blaBlaCarCountry;

    @Bean(name = "blaBlaCarApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setAuthorization(authorization);
        builder.setHost(blaBlaCarHost);
        builder.setPathVariable(blaBlaCarPathVariable);
        builder.setJourneyPathVariable(blaBlaCarJourneyPathVariable);
        builder.setApiVersion(blaBlaCarApiVersion);
        builder.setProtocol(accessProtocol);
        builder.setLanguage(blaBlaCarLanguage);
        builder.setCurrency(blaBlaCarCurrency);
        builder.setRadius(blaBlaCarRadius);
        builder.setNumberOfPersons(blaBlaCarNumberOfPersons);
        builder.setResultLength(blaBlaCarResultLength);
        builder.setTimeIsDeparture(blaBlaCarTimeIsDeparture);
        builder.setSortDirection(blaBlaCarSortDirection);
        builder.setCountry(blaBlaCarCountry);

        return builder.build();
    }
}
