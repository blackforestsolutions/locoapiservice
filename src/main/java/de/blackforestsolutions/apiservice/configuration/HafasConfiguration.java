package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class HafasConfiguration {

    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${hafasLanguage}")
    private String hafasLanguage;
    @Value("${hafasLocationMethod}")
    private String hafasLocationMethod;
    @Value("${hafasLocationResultLength}")
    private String hafasLocationResultLength;
    @Value("${hafasVersion}")
    private String hafasVersion;
    @Value("${hafasPathVariable}")
    private String hafasPathVariable;
    @Value("${hafasAllowIntermediateStops}")
    private boolean hafasAllowIntermediateStops;
    @Value("${hafasTransfers}")
    private int hafasTransfers;
    @Value("${hafasMinTransfersTime}")
    private int hafasMinTransfersTime;
    @Value("${hafasForDisabledPersons}")
    private String hafasForDisabledPersons;
    @Value("${hafasAllowTariffDetails}")
    private boolean hafasAllowTariffDetails;
    @Value("${hafasAllowCoordinates}")
    private boolean hafasAllowCoordinates;
    @Value("${hafasJourneyMethod}")
    private String hafasJourneyMethod;


    @Bean(name = "hafasStandardConfiguration")
    public ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder setHafasStandardConfiguration() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accessProtocol);
        builder.setLanguage(hafasLanguage);
        builder.setResultLength(Integer.parseInt(hafasLocationResultLength));
        builder.setLocationPath(hafasLocationMethod);
        builder.setApiVersion(hafasVersion);
        builder.setPathVariable(hafasPathVariable);
        builder.setAllowIntermediateStops(hafasAllowIntermediateStops);
        builder.setTransfers(hafasTransfers);
        builder.setMinTransferTime(hafasMinTransfersTime);
        builder.setForDisabledPersons(hafasForDisabledPersons);
        builder.setAllowTariffDetails(hafasAllowTariffDetails);
        builder.setAllowCoordinates(hafasAllowCoordinates);
        builder.setJourneyPathVariable(hafasJourneyMethod);
        return builder;
    }
}
