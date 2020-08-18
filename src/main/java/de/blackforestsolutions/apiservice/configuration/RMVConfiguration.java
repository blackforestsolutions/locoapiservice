package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;

@SpringBootConfiguration
public class RMVConfiguration {

    @Value("${rmvAuthorization}")
    private String authorization;
    @Value("${rmvHost}")
    private String host;
    @Value("${rmvpathlocation}")
    private String rmvPathLocation;
    @Value("${rmvpathtrip}")
    private String rmvPathTrip;
    @Value("${rmvpathcoordinates}")
    private String rmvPathCoordinates;
    @Value("${accessProtocol}")
    private String accesProtocol;
    @Value("${rmvLanguage}")
    private String rmvLanguage;
    @Value("${rmvRadius}")
    private String rmvRadius;
    @Value("${rmvStationTypes}")
    private String rmvStationTypes;
    @Value("${rmvTimeIsDeparture}")
    private boolean rmvTimeIsDeparture;
    @Value("${rmvResultLengthBeforeDate}")
    private Integer rmvResultLengthBeforeDate;
    @Value("${rmvResultLengthAfterDate}")
    private Integer rmvResultLengthAfterDate;
    @Value("${rmvAllowIntermediateStops}")
    private boolean rmvAllowIntermediateStops;

    @Value("${rmvLeftTopLatitude}")
    private double rmvLeftTopLatitude;
    @Value("${rmvLeftTopLongitude}")
    private double rmvLeftTopLongitude;
    @Value("${rmvRightBottomLatitude}")
    private double rmvRightBottomLatitude;
    @Value("${rmvRightBottomLongitude}")
    private double rmvRightBottomLongitude;

    @Bean(name = "rmvApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(accesProtocol);
        builder.setHost(host);
        builder.setPort(0);
        builder.setPathVariable(rmvPathLocation);
        builder.setLocationPath(rmvPathLocation);
        builder.setCoordinatesPath(rmvPathCoordinates);
        builder.setJourneyPathVariable(rmvPathTrip);
        builder.setAuthorizationKey(HttpHeaders.AUTHORIZATION);
        builder.setAuthorization(authorization);
        builder.setLanguage(rmvLanguage);
        builder.setRadius(Integer.parseInt(rmvRadius));
        builder.setOutputFormat(rmvStationTypes);
        builder.setTimeIsDeparture(rmvTimeIsDeparture);
        builder.setResultLengthBeforeDepartureTime(rmvResultLengthBeforeDate);
        builder.setResultLengthAfterDepartureTime(rmvResultLengthAfterDate);
        builder.setAllowIntermediateStops(rmvAllowIntermediateStops);
        return builder.build();
    }

    @Bean(name = "rmvBox")
    public Box box() {
        Point leftTop = new Point(rmvLeftTopLongitude, rmvLeftTopLatitude);
        Point rightBottom = new Point(rmvRightBottomLongitude, rmvRightBottomLatitude);
        return new Box(leftTop, rightBottom);
    }
}