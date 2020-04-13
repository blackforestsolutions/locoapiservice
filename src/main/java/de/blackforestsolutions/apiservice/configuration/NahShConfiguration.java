package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class NahShConfiguration {

    private final ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration;
    @Value("${hafasNahShHost}")
    private String hafasNahShHost;
    @Value("${hafasNahShClientId}")
    private String hafasNahShClientId;
    @Value("${hafasNahShClientVersion}")
    private String hafasNahShClientVersion;
    @Value("${hafasNahShClientName}")
    private String hafasNahShClientName;
    @Value("${hafasNahShAuthorization}")
    private String hafasNahShAuthorization;
    @Value("${hafasNahShProductionValue}")
    private String hafasNahShProductionValue;
    @Value("${hafasNahShResultsAfterDepartureTime}")
    private int hafasNahShResultsAfterDepartureTime;
    @Value("${hafasNahShTimeIsDeparture}")
    private boolean hafasNahShTimeIsDeparture;

    @Autowired
    public NahShConfiguration(ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder hafasStandardConfiguration) {
        this.hafasStandardConfiguration = hafasStandardConfiguration;
    }

    @Bean(name = "nahShApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        hafasStandardConfiguration.setHost(hafasNahShHost);
        hafasStandardConfiguration.setCliendId(hafasNahShClientId);
        hafasStandardConfiguration.setClientVersion(hafasNahShClientVersion);
        hafasStandardConfiguration.setClientName(hafasNahShClientName);
        hafasStandardConfiguration.setAuthorization(hafasNahShAuthorization);
        hafasStandardConfiguration.setHafasProductionValue(hafasNahShProductionValue);
        hafasStandardConfiguration.setResultLengthAfterDepartureTime(hafasNahShResultsAfterDepartureTime);
        hafasStandardConfiguration.setTimeIsDeparture(hafasNahShTimeIsDeparture);
        return hafasStandardConfiguration.build();
    }
}
