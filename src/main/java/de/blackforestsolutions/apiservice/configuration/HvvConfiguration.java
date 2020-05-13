package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class HvvConfiguration {

    @Value("${hvvHost}")
    private String hvvHost;
    @Value("${hvvPathVariable}")
    private String hvvPathVariable;
    @Value("${hvvStationsPathVariable}")
    private String hvvStationsPathVariable;
    @Value("${hvvJourneyPathVariable}")
    private String hvvJourneyPathVariable;
    @Value("${hvvTravelPointPathVariable}")
    private String hvvTravelPointPathVariable;
    @Value("${hvvAuthentificationType}")
    private String hvvAuthentificationType;
    @Value("${hvvAuthentificationUser}")
    private String hvvAuthentificationUser;
    @Value("${hvvAuthentificationPassword}")
    private String hvvAuthentificationPassword;
    @Value("${hvvApiVersion}")
    private String hvvApiVersion;
    @Value("${hvvLanguage}")
    private String hvvLanguage;
    @Value("${hvvTarif}")
    private String hvvTarif;
    @Value("${hvvAccessProtocol}")
    private String hvvAccessProtocol;
    @Value("${hvvFilterEquivalent}")
    private boolean hvvFilterEquivalent;
    @Value("${hvvAllowTypeSwitch}")
    private boolean hvvAllowTypeSwitch;
    @Value("${allowTariffDetails}")
    private boolean allowTariffDetails;
    @Value("${allowReducedPrice}")
    private boolean allowReducedPrice;
    @Value("${allowIntermediateStops}")
    private boolean allowIntermediateStops;
    @Value(("${hvvReturnContSearchData}"))
    private boolean hvvReturnContSearchData;
    @Value("${hvvResultLength}")
    private int hvvResultLength;
    @Value("${hvvMaxDistanceFromCoordinate}")
    private int hvvMaxDistanceFromCoordinate;

    @Bean(name = "hvvApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setHost(hvvHost);
        builder.setPathVariable(hvvPathVariable);
        builder.setStationListPathVariable(hvvStationsPathVariable);
        builder.setJourneyPathVariable(hvvJourneyPathVariable);
        builder.setTravelPointPathVariable(hvvTravelPointPathVariable);
        builder.setAuthentificationType(hvvAuthentificationType);
        builder.setAuthentificationUser(hvvAuthentificationUser);
        builder.setAuthentificationPassword(hvvAuthentificationPassword);
        builder.setApiVersion(hvvApiVersion);
        builder.setLanguage(hvvLanguage);
        builder.setTariff(hvvTarif);
        builder.setProtocol(hvvAccessProtocol);
        builder.setHvvFilterEquivalent(hvvFilterEquivalent);
        builder.setHvvAllowTypeSwitch(hvvAllowTypeSwitch);
        builder.setAllowTariffDetails(allowTariffDetails);
        builder.setAllowReducedPrice(allowReducedPrice);
        builder.setAllowIntermediateStops(allowIntermediateStops);
        builder.setHvvReturnContSearchData(hvvReturnContSearchData);
        builder.setResultLength(hvvResultLength);
        builder.setDistanceFromTravelPoint(hvvMaxDistanceFromCoordinate);
        return builder.build();
    }
}

