package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SearchChConfiguration {

    @Value("${searchChHost}")
    private String searchChHost;

    @Value("${searchChAccessProtocol}")
    private String searchChAccessProtocol;

    @Value("${searchChPathVariable}")
    private String searchChPathVariable;

    @Value("${searchChTermParameter}")
    private String searchChTermParameter;

    @Value("${searchChStationIdParameter}")
    private String searchChStationIdParameter;

    @Value("${searchChStationCoordinateParameter}")
    private String searchChStationCoordinateParameter;

    @Value("${searchChLocationPathVariable}")
    private String searchChLocationPathVariable;

    @Value("${searchChRoutePathVariable}")
    private String searchChRoutePathVariable;

    @Value("${searchChResults}")
    private String searchChResults;

    @Value("${searchChDelayParameter}")
    private String searchChDelayParameter;

    @Value("departure")
    private String departure;

    @Value("arrival")
    private String arrival;

    @Value("${datePathVariable}")
    private String datePathVariable;

    @Value("${timePathVariable}")
    private String timePathVariable;

    @Bean(name = "searchApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builderConfig = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builderConfig.setProtocol(searchChAccessProtocol);
        builderConfig.setPathVariable(searchChPathVariable);
        builderConfig.setLocationPath(searchChLocationPathVariable);
        builderConfig.setHost(searchChHost);
        builderConfig.setSearchChTermParameter(searchChTermParameter);
        builderConfig.setSearchChStationId(searchChStationIdParameter);
        builderConfig.setSearchChStationCoordinateParameter(searchChStationCoordinateParameter);
        builderConfig.setSearchChDelayParameter(searchChDelayParameter);
        builderConfig.setSearchChResults(searchChResults);
        builderConfig.setJourneyPathVariable(searchChRoutePathVariable);
        builderConfig.setArrival(arrival);
        builderConfig.setDeparture(departure);
        builderConfig.setDatePathVariable(datePathVariable);
        builderConfig.setTimePathVariable(timePathVariable);
        return builderConfig.build();
    }


}
