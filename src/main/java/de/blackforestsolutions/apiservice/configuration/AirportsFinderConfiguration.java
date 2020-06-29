package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AirportsFinderConfiguration {
    private static final int CONFIGURED_BASE_PORT = 0;
    @Value("${accessProtocol}")
    private String accessProtocol;
    @Value("${xRapidapiKeyValue}")
    private String xRapidapiKeyValue;
    @Value("${airportsFinderHostPart1}")
    private String airportsFinderHostPart1;
    @Value("${airportsFinderHostPart2ApiVersion}")
    private String airportsFinderHostPart2ApiVersion;
    @Value("${airportsFinderHostPart3}")
    private String airportsFinderHostPart3;
    @Value("${airportsFinderVariable}")
    private String airportsFinderVariable;
    @Value("${xRapidapiKey}")
    private String xRapidapiKey;


    @Bean(name = "airportsFinderApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        String airportsFinderHostFull = buildHost(airportsFinderHostPart1, airportsFinderHostPart2ApiVersion, airportsFinderHostPart3);
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accessProtocol);
        builder.setHost(airportsFinderHostFull);
        builder.setPort(CONFIGURED_BASE_PORT);
        builder.setPathVariable(airportsFinderVariable);
        builder.setAuthorization(xRapidapiKeyValue);
        builder.setAuthorizationKey(xRapidapiKey);
        return builder.build();
    }

    private String buildHost(String airportsFinderHostPart1, String airportsFinderHostPart2ApiVersion, String airportsFinderHostPart3) {
        return airportsFinderHostPart1.concat(airportsFinderHostPart2ApiVersion).concat(airportsFinderHostPart3);
    }

}
