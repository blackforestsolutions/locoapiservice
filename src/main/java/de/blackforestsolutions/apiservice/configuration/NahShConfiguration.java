package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class NahShConfiguration {

    private final ApiTokenAndUrlInformation hafasStandardConfiguration;

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
    public NahShConfiguration(ApiTokenAndUrlInformation hafasStandardConfiguration) {
        this.hafasStandardConfiguration = hafasStandardConfiguration;
    }

    @Bean(name = "nahShApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(hafasStandardConfiguration);
        builder.setHost(hafasNahShHost);
        builder.setClientId(hafasNahShClientId);
        builder.setClientVersion(hafasNahShClientVersion);
        builder.setClientName(hafasNahShClientName);
        builder.setAuthorization(hafasNahShAuthorization);
        builder.setHafasProductionValue(hafasNahShProductionValue);
        builder.setResultLengthAfterDepartureTime(hafasNahShResultsAfterDepartureTime);
        builder.setTimeIsDeparture(hafasNahShTimeIsDeparture);
        return builder.build();
    }
}
