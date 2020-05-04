package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFromPatternAndString;

@Slf4j
public class ApiTokenAndUrlInformationObjectMother {


    public static ApiTokenAndUrlInformation getOSMApiTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("nominatim.openstreetmap.org");
        builder.setDeparture("Stuttgart, Waiblinger Str. 84");
        builder.setArrival("Stuttgart, Waiblinger Str. 84");
        builder.setOutputFormat("json");
        builder.setResultLength(1);
        builder.setPath("/?addressdetails=1&q=Stuttgart,+Waiblinger+Str.+84&format=json&limit=1");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getOSMApiTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("nominatim.openstreetmap.org");
        builder.setDeparture("Stuttgart, Waiblinger Str. 84");
        builder.setArrival("Stuttgart, Waiblinger Str. 84");
        builder.setOutputFormat("json");
        builder.setResultLength(1);
        builder.setPath("/?addressdetails=1&q=Stuttgart,+Waiblinger+Str.+84&format=json&limit=1");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getNahShTokenAndUrl(String departure, String arrival) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = getHafasStandardTokenAndUrl();
        builder.setHost("nah.sh.hafas.de");
        builder.setClientId("NAHSH");
        builder.setClientVersion("3000700");
        builder.setClientName("NAHSHPROD");
        builder.setAuthorization("r0Ot9FLFNAFxijLW");
        builder.setDeparture(departure);
        builder.setArrival(arrival);
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-02-13 11:46:50"));
        } catch (ParseException e) {
            log.error("Error While parsing Date", e);
        }
        builder.setHafasProductionValue("1023");
        builder.setResultLengthAfterDepartureTime(1);
        builder.setTimeIsDeparture(true);
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getVBBTokenAndUrl(String departure, String arrival) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = getHafasStandardTokenAndUrl();
        builder.setHost("fahrinfo.vbb.de");
        builder.setApiName("VBB.1");
        builder.setMic("mic");
        builder.setMac("mac");
        builder.setClientId("VBB");
        builder.setClientVersion("4010300");
        builder.setClientName("vbbPROD");
        builder.setClientType("IPA");
        builder.setDeparture(departure);
        builder.setArrival(arrival);
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-02-14 09:26:53"));
        } catch (ParseException e) {
            log.error("Error While parsing Date", e);
        }
        builder.setAuthentificationType("AID");
        builder.setAuthorization("hafas-vbb-apps");
        builder.setDeparture(departure);
        builder.setArrival(arrival);
        builder.setAuthorizationKey("5243544a4d3266467846667878516649");
        builder.setHafasProductionValue("127");
        builder.setWalkingSpeed("foot_speed_normal");
        builder.setResultLengthAfterDepartureTime(1);
        builder.setTimeIsDeparture(true);
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getDBTokenAndUrl(String departure, String arrival) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = getHafasStandardTokenAndUrl();
        builder.setHost("reiseauskunft.bahn.de");
        builder.setApiName("DB.R19.04.a");
        builder.setChecksum("checksum");
        builder.setHafasRtMode("HYBRID");
        builder.setClientId("DB");
        builder.setClientVersion("16040000");
        builder.setClientName("DB Navigator");
        builder.setClientType("IPH");
        builder.setDeparture(departure);
        builder.setArrival(arrival);
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-02-14 09:44:29"));
        } catch (ParseException e) {
            log.error("Error While parsing Date", e);
        }
        builder.setAuthentificationType("AID");
        builder.setAuthorization("n91dB8Z77MLdoR0K");
        builder.setAuthorizationKey("bdI8UVj40K5fvxwf");
        builder.setHafasProductionValue("1023");
        builder.setResultLengthAfterDepartureTime(1);
        builder.setTimeIsDeparture(true);
        builder.setAllowReducedPrice(true);
        return builder.build();
    }

    private static ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder getHafasStandardTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setPort(0);
        builder.setLanguage("de");
        builder.setResultLength(1);
        builder.setLocationPath("LocMatch");
        builder.setJourneyPathVariable("TripSearch");
        builder.setApiVersion("1.16");
        builder.setPathVariable("bin/mgate.exe?");
        builder.setAllowIntermediateStops(true);
        builder.setTransfers(-1);
        builder.setMinTransferTime(0);
        builder.setForDisabledPersons("notBarrierfree");
        builder.setAllowTariffDetails(true);
        builder.setAllowCoordinates(false);
        return builder;
    }

    public static ApiTokenAndUrlInformation getSearchChStationTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("fahrplan.search.ch");
        builder.setPort(8080);
        builder.setPathVariable("api");
        builder.setLocationPath("completion.json");
        builder.setSearchChTermParameter("term");
        builder.setLocationSearchTerm("lu");
        builder.setSearchChStationId("show_ids=1");
        builder.setSearchChStationCoordinateParameter("show_coordinates=1");
        builder.setPath("/api/completion.json?term=lu");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getSearchChStationTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("fahrplan.search.ch");
        builder.setPort(0);
        builder.setPathVariable("api");
        builder.setLocationPath("completion.json");
        builder.setSearchChTermParameter("term");
        builder.setLocationSearchTerm("lu");
        builder.setSearchChStationId("show_ids");
        builder.setSearchChStationCoordinateParameter("show_coordinates");
        builder.setPath("/api/completion.json?term=lu");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getSearchChRouteTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("fahrplan.search.ch");
        builder.setPort(8080);
        builder.setPathVariable("api");
        builder.setSearchChRoutePathVariable("route.json");
        builder.setDeparture("from");
        builder.setStartLocation("8503283");
        builder.setArrival("to");
        builder.setDestinationLocation("Zürich,+Förrlibuckstr.+60");
        builder.setDatePathVariable("date");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse("2019-11-04-14-00-00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setTimePathVariable("time");
        builder.setSearchChDelayParameter("show_delays=1");
        builder.setSearchChResults("num=1");
        builder.setPath("/api/route.json?from=8503283&to=Zürich,+Förrlibuckstr.+60&date=04.11.2019&time=14:00&show_delays=1&num=1");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getSearchChRouteTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("fahrplan.search.ch");
        builder.setPort(0);
        builder.setPathVariable("api");
        builder.setSearchChRoutePathVariable("route.json");
        builder.setDeparture("from");
        builder.setStartLocation("8503283");
        builder.setArrival("to");
        builder.setDestinationLocation("Zürich,+Förrlibuckstr.+60");
        builder.setDatePathVariable("date");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse("2019-11-04-14-00-00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setTimePathVariable("time");
        builder.setSearchChDelayParameter("show_delays=1");
        builder.setSearchChResults("num=1");
        builder.setPath("/api/route.json?from=8503283&to=Zürich,+Förrlibuckstr.+60&date=04.11.2019&time=14:00&show_delays=1&num=1");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getHvvStationListTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("http");
        builder.setHost("api-test.geofox.de");
        builder.setPathVariable("gti/public");
        builder.setStationListPathVariable("/listStations");
        builder.setAuthentificationType("HmacSHA1");
        builder.setAuthentificationUser("janhendrikhausner");
        builder.setAuthentificationPassword("R!7aP2YUK3yD");
        builder.setApiVersion("37.3");
        builder.setLanguage("de");
        builder.setPath("/gti/public/listStations");
        builder.setHvvAllowTypeSwitch(true);
        builder.setAllowTariffDetails(true);
        builder.setHvvFilterEquivalent(true);

        return builder.build();
    }

    public static ApiTokenAndUrlInformation getHvvTravelpointTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("http");
        builder.setHost("api-test.geofox.de");
        builder.setPathVariable("gti/public");
        builder.setTravelPointPathVariable("/checkName");
        builder.setAuthentificationType("HmacSHA1");
        builder.setAuthentificationUser("janhendrikhausner");
        builder.setAuthentificationPassword("R!7aP2YUK3yD");
        builder.setApiVersion("37.3");
        builder.setLanguage("de");
        builder.setResultLength(1);
        builder.setDistanceFromTravelPoint(2500);
        builder.setDeparture("Große Bleichen 25");
        builder.setPath("/gti/public/checkName");
        builder.setHvvAllowTypeSwitch(true);
        builder.setAllowTariffDetails(true);

        return builder.build();
    }

    public static ApiTokenAndUrlInformation getHvvJourneyTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("http");
        builder.setHost("api-test.geofox.de");
        builder.setPathVariable("gti/public");
        builder.setJourneyPathVariable("/getRoute");
        builder.setTravelPointPathVariable("/checkName");
        builder.setAuthentificationType("HmacSHA1");
        builder.setAuthentificationUser("janhendrikhausner");
        builder.setAuthentificationPassword("R!7aP2YUK3yD");
        builder.setApiVersion("37.3");
        builder.setLanguage("de");
        builder.setPath("/gti/public/getRoute");
        builder.setResultLength(1);
        builder.setDistanceFromTravelPoint(2500);
        builder.setDeparture("Ahrensburg");
        builder.setArrival("Stadthausbrücke");
        builder.setAllowTariffDetails(true);
        builder.setHvvAllowTypeSwitch(true);
        try {
            builder.setDepartureDate(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "31.01.2020 13:10"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setResultLengthBeforeDepartureTime(2);
        builder.setResultLengthAfterDepartureTime(2);
        builder.setTariff("all");
        builder.setTimeIsDeparture(true);
        builder.setAllowReducedPrice(true);
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getRMVTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("www.rmv.de");
        builder.setPath("hapi/location.name?");
        builder.setGermanRailJourneyDeatilsPath("hapi/trip?");
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e");
        builder.setArrival("frankfurt hauptbahnhof");
        builder.setDeparture("Lorch-Lorchhausen Bahnhof");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation configurationToken() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("www.rmv.de");
        builder.setPort(0);
        builder.setPathVariable("hapi/location.name?");
        builder.setLocationPath("hapi/location.name?");
        builder.setGermanRailJourneyDeatilsPath("hapi/trip?");
        builder.setAuthorizationKey(HttpHeaders.AUTHORIZATION);
        builder.setAuthorization("1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation requestInfos() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setDeparture("testDeparture");
        builder.setArrival("testArrival");
        builder.setArrivalDate(Date.from(Instant.ofEpochSecond(10)));
        builder.setDepartureDate(Date.from(Instant.ofEpochSecond(1)));
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnRailwayStationTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPort(8080);
        builder.setApiVersion("v1");
        builder.setPathVariable("fahrplan-plus");
        builder.setGermanRailLocationPath("location");
        builder.setBahnLocation("Berlin");
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/location/Berlin");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnRailwayStationTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setApiVersion("v1");
        builder.setPort(0);
        builder.setPathVariable("fahrplan-plus");
        builder.setGermanRailLocationPath("location");
        builder.setBahnLocation("Berlin");
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/location/Berlin");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnArrivalBoardTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailArrivalBoardPath("arrivalBoard");
        builder.setStationId("8011160");
        try {
            builder.setGermanRailDatePathVariable(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPort(8080);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/arrivalBoard/8011160?date=2019-07-25");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnArrivalBoardTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailArrivalBoardPath("arrivalBoard");
        builder.setStationId("8011160");
        try {
            builder.setGermanRailDatePathVariable(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPort(0);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/arrivalBoard/8011160?date=2019-07-25");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnDepartureBoardTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailDepartureBoardPath("departureBoard");
        builder.setStationId("8011160");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPort(8080);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/departureBoard/8011160?date=2019-07-25");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnDepartureBoardTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailDepartureBoardPath("departureBoard");
        builder.setStationId("8011160");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPort(0);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/departureBoard/8011160?date=2019-07-25");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnJourneyDetailsTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailJourneyDeatilsPath("journeyDetails");
        builder.setJourneyDetailsId("715770%2F254084%2F898562%2F210691%2F80%3fstation_evaId%3D8000312");
        builder.setPort(8080);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/journeyDetails/715770%252F254084%252F898562%252F210691%252F80%253fstation_evaId%253D8000312");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBahnJourneyDetailsTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.deutschebahn.com");
        builder.setPathVariable("fahrplan-plus");
        builder.setApiVersion("v1");
        builder.setGermanRailJourneyDeatilsPath("journeyDetails");
        builder.setJourneyDetailsId("715770%2F254084%2F898562%2F210691%2F80%3fstation_evaId%3D8000312");
        builder.setPort(0);
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8");
        builder.setPath("/fahrplan-plus/v1/journeyDetails/715770%252F254084%252F898562%252F210691%252F80%253fstation_evaId%253D8000312");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getLufthansaTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.lufthansa.com");
        builder.setPort(8080);
        builder.setApiVersion("v1");
        builder.setPathVariable("operations/schedules");
        builder.setDeparture("ZRH");
        builder.setArrival("FRA");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-28"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-28"));
        } catch (ParseException e) {
            log.error("Error While parsing Date", e);
        }
        builder.setAuthorizationKey("Authorization");
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setAuthorization("Bearer guuryzmgpftrnum76twe3y7k");
        builder.setPath("/v1/operations/schedules/ZRH/FRA/2019-12-28");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getLufthansaTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.lufthansa.com");
        builder.setApiVersion("v1");
        builder.setPathVariable("operations/schedules");
        builder.setDeparture("ZRH");
        builder.setArrival("FRA");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
        } catch (ParseException e) {
            log.error("Error While parsing Date", e);
        }
        builder.setAuthorizationKey("Authorization");
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setAuthorization("Bearer n82nfwuh9jnpkmvrf7trbt3j");
        builder.setPath("/v1/operations/schedules/ZRH/FRA/2019-12-28");
        return builder.build();
    }


    public static ApiTokenAndUrlInformation getHazelcastTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("http");
        builder.setHost("localhost");
        builder.setPort(8081);
        builder.setHazelcastPath("/hazelcast/read-data");
        builder.setHazelcastReadAllPath("/hazelcast/read-all-data");
        builder.setHazelcastSearchPath("/hazelcast/read-data");
        builder.setHazelcastWritePath("/hazelcast/write-data");
        builder.setPathVariable("operations/schedules");
        builder.setPath("operations/schedules");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBritishAirwaysTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.ba.com");
        builder.setPort(8080);
        builder.setApiVersion("rest-v1/v1");
        builder.setPathVariable("flights");
        builder.setDeparture("lhr");
        builder.setArrival("txl");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-20"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-20"));
        } catch (ParseException e) {
            log.info("unable to parse british airways token: " + e);
        }
        builder.setAuthorizationKey("client-key");
        builder.setAuthorization("64x9epryst4b4g2aaks4b3yn");
        builder.setPath("/rest-v1/v1/flights;departureLocation=lhr;arrivalLocation=txl;scheduledDepartureDate=2019-10-20");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBritishAirwaysTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.ba.com");
        builder.setApiVersion("rest-v1/v1");
        builder.setPathVariable("flights");
        builder.setDeparture("lhr");
        builder.setArrival("txl");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-20"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-20"));
        } catch (ParseException e) {
            log.info("unable to parse british airways token: " + e);
        }
        builder.setAuthorizationKey("client-key");
        builder.setAuthorization("64x9epryst4b4g2aaks4b3yn");
        builder.setPath("/rest-v1/v1/flights;departureLocation=lhr;arrivalLocation=txl;scheduledDepartureDate=2019-10-20");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBbcTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("public-api.blablacar.com");
        builder.setPort(8080);
        builder.setApiVersion("api/v2");
        builder.setPathVariable("trips?");
        builder.setDeparture("ZRH");
        builder.setArrival("FRA");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
        } catch (ParseException e) {
            log.info("unable to parse bbc token: " + e);
        }
        builder.setAuthorizationKey("key"); //FRAGE!
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setAuthorization("7f529ec36ab542b78e63f5270a621837");
        builder.setPath("/api/v2/trips");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBbcTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("public-api.blablacar.com");
        builder.setApiVersion("api/v2");
        builder.setPathVariable("trips?");
        builder.setDeparture("ZRH");
        builder.setArrival("FRA");
        try {
            builder.setDepartureDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
            builder.setArrivalDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-28"));
        } catch (ParseException e) {
            log.info("unable to parse bbc token: " + e);
        }
        builder.setAuthorizationKey("key");
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setAuthorization("7f529ec36ab542b78e63f5270a621837");
        builder.setPath("/api/v2/trips");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getAirportsFinderTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("https");
        builder.setHost("cometari-airportsfinder-v1.p.rapidapi.com");
        builder.setPort(8080);
        builder.setPathVariable("api/airports/by-radius");
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        departureCoordinates.setLatitude(48.1301564);
        departureCoordinates.setLongitude(8.2324351);
        builder.setDepartureCoordinates(departureCoordinates.build());

        builder.setAuthorizationKey("x-rapidapi-key");
        builder.setAuthorization("b441403e78mshfe074d6ec0c2af2p1be89cjsn04932ccb889e");
        builder.setPath("/api/airports/by-radius;radius=300;lng=8.2324351;lat=48.1301564");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getAirportsFinderTokenAndUrlIT() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("https");
        builder.setHost("cometari-airportsfinder-v1.p.rapidapi.com");
        builder.setPathVariable("api/airports/by-radius");
        Coordinates.CoordinatesBuilder departureCoordinates = new Coordinates.CoordinatesBuilder();
        departureCoordinates.setLatitude(48.1301564);
        departureCoordinates.setLongitude(8.2324351);
        builder.setDepartureCoordinates(departureCoordinates.build());

        builder.setAuthorizationKey("x-rapidapi-key");
        builder.setAuthorization("b441403e78mshfe074d6ec0c2af2p1be89cjsn04932ccb889e");
        builder.setPath("/api/airports/by-radius?radius=300&lng=8.2324351&lat=48.1301564");
        return builder.build();
    }
}
