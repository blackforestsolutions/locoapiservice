package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class FlinksterConfiguration {

    @Value("${germanRailAuthorization}")
    private String germanRailAuthorization;
    @Value("${germanRailHost}")
    private String germanRailHost;
    @Value("${germanRailApiVersion}")
    private String germanRailApiVersion;
    @Value("${accessProtocol}")
    private String accesProtocol;
    @Value("${flinksterPathVariable}")
    private String flinksterPathVariable;
    @Value("${flinksterProvidernetwork}")
    private String flinksterProvidernetwork;
    @Value("${flinksterSearchRadius}")
    private int flinksterSearchRadius;


    @Bean(name = "flinksterApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setAuthorization(germanRailAuthorization);
        builder.setProtocol(accesProtocol);
        builder.setApiVersion(germanRailApiVersion);
        builder.setHost(germanRailHost);
        builder.setPathVariable(flinksterPathVariable);
        builder.setCliendId(flinksterProvidernetwork);
        builder.setDistanceFromTravelPoint(flinksterSearchRadius);
        return builder.build();
    }
}
