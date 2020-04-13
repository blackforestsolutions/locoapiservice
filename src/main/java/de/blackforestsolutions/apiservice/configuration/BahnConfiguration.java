package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BahnConfiguration {

    @Value("${germanRailAuthorization}")
    private String germanRailAuthorization;
    @Value("${germanRailHost}")
    private String germanRailHost;
    @Value("${germanRailTimetablePathVariable}")
    private String germanRailTimetablePathVariable;
    @Value("${germanRailApiVersion}")
    private String germanRailApiVersion;
    @Value("${germanRailLocationPath}")
    private String germanRailLocationPath;
    @Value("$germanRailAccessProtocol")
    private String accessProtocol;
    @Value("${germanRailArrivalBoardPath}")
    private String germanRailArrivalBoardVariable;
    @Value("${germanRailDepartureBoardPath}")
    private String germanRailDepartureBoardVariable;
    @Value("${germanRailJourneyDetailsPath}")
    private String germanRailJourneyDetailsVariable;


    @Bean(name = "bahnApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setAuthorization(germanRailAuthorization);
        builder.setProtocol(accessProtocol);
        builder.setApiVersion(germanRailApiVersion);
        builder.setHost(germanRailHost);
        builder.setPathVariable(germanRailTimetablePathVariable);
        builder.setGermanRailLocationPath(germanRailLocationPath);
        builder.setGermanRailDepartureBoardPath(germanRailDepartureBoardVariable);
        builder.setGermanRailArrivalBoardPath(germanRailArrivalBoardVariable);
        builder.setGermanRailJourneyDeatilsPath(germanRailJourneyDetailsVariable);
        return builder.build();
    }


}
