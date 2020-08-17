package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFromLocalDatePatternAndString;

@Slf4j
public class ApiTokenAndUrlInformationObjectMother {

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
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-02-13 11:46:50"));
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
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-02-14 09:26:53"));
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
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-02-14 09:44:29"));
        builder.setAuthentificationType("AID");
        builder.setAuthorization("n91dB8Z77MLdoR0K");
        builder.setAuthorizationKey("bdI8UVj40K5fvxwf");
        builder.setHafasProductionValue("1023");
        builder.setResultLengthAfterDepartureTime(1);
        builder.setTimeIsDeparture(true);
        builder.setAllowReducedPrice(true);
        builder.setPath("/bin/mgate.exe?checksum=6d0c117c5a7f98d00582bc4a9be5dad1");
        return builder.build();
    }

    private static ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder getHafasStandardTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
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

    public static ApiTokenAndUrlInformation getHvvTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();

        builder.setProtocol("http");
        builder.setHost("api-test.geofox.de");
        builder.setPathVariable("gti/public");
        builder.setTravelPointPathVariable("/checkName");
        builder.setJourneyPathVariable("/getRoute");
        builder.setAuthentificationType("HmacSHA1");
        builder.setAuthentificationUser("janhendrikhausner");
        builder.setAuthentificationPassword("R!7aP2YUK3yD");
        builder.setApiVersion("37.3");
        builder.setLanguage("de");
        builder.setResultLength(1);
        builder.setDistanceFromTravelPoint(2500);
        builder.setDeparture("Ahrensburg");
        builder.setArrival("Stadthausbrücke");
        builder.setPath("/gti/public/checkName");
        builder.setHvvAllowTypeSwitch(true);
        builder.setAllowTariffDetails(true);
        builder.setHvvFilterEquivalent(true);
        builder.setResultLength(1);
        builder.setDistanceFromTravelPoint(2500);
        builder.setHvvAllowTypeSwitch(true);
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "31.01.2020 13:10"));
        builder.setResultLengthBeforeDepartureTime(2);
        builder.setResultLengthAfterDepartureTime(2);
        builder.setTariff("all");
        builder.setTimeIsDeparture(true);
        builder.setAllowReducedPrice(true);
        builder.setAllowIntermediateStops(true);
        builder.setHvvReturnContSearchData(true);
        builder.setPath("/gti/public/getRoute");

        return builder.build();
    }

    public static ApiTokenAndUrlInformation getRMVTokenAndUrl(String departure, String arrival) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("www.rmv.de");
        builder.setLocationPath("hapi/location.name?");
        builder.setCoordinatesPath("hapi/location.nearbystops?");
        builder.setGermanRailJourneyDeatilsPath("hapi/trip?");
        builder.setAuthorizationKey("Authorization");
        builder.setAuthorization("1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e");
        builder.setLanguage("de");
        builder.setArrival(arrival);
        builder.setArrivalDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm", "2020-05-04 08:00"));
        builder.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(50.01d, 50.01d).build());
        builder.setDeparture(departure);
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm", "2020-05-04 08:00"));
        builder.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(50.052278d, 8.571331d).build());
        builder.setRadius(1000);
        builder.setOutputFormat("SP");
        builder.setTimeIsDeparture(true);
        builder.setResultLengthBeforeDepartureTime(2);
        builder.setResultLengthAfterDepartureTime(4);
        builder.setAllowIntermediateStops(true);
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
        builder.setArrivalDate(ZonedDateTime.parse("2020-06-29T10:15:30+01:00"));
        builder.setDepartureDate(ZonedDateTime.parse("2020-06-29T10:15:30+01:00"));
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getLufthansaTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("api.lufthansa.com");
        builder.setApiVersion("v1");
        builder.setPathVariable("oauth/token");
        builder.setJourneyPathVariable("operations/schedules");
        builder.setDeparture("ZRH");
        builder.setArrival("FRA");
        builder.setDepartureDate(generateDateFromLocalDatePatternAndString(DateTimeFormatter.ISO_LOCAL_DATE, "2019-06-28"));
        builder.setArrivalDate(generateDateFromLocalDatePatternAndString(DateTimeFormatter.ISO_LOCAL_DATE, "2019-06-28"));
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setClientId("v6tqqk92k2a6zzaharpz56x6");
        builder.setClientSecret("c9kUKEuT3k");
        builder.setClientType("client_credentials");
        builder.setAuthorization("fhfdds7fskppmt6xn2z423a9");
        builder.setPath("/v1/operations/schedules/ZRH/FRA/2019-12-28");
        builder.setAuthorizationKey("Authorization");
        builder.setXOriginationIp("88.66.47.47");
        builder.setXOriginationIpKey("X-Originating-IP");
        builder.setAuthorization("Bearer guuryzmgpftrnum76twe3y7k");
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
        builder.setApiVersion("rest-v1/v1");
        builder.setPathVariable("flights");
        builder.setDeparture("lhr");
        builder.setArrival("txl");
        builder.setDepartureDate(generateDateFromLocalDatePatternAndString(DateTimeFormatter.ISO_LOCAL_DATE, "2019-10-20"));
        builder.setArrivalDate(generateDateFromLocalDatePatternAndString(DateTimeFormatter.ISO_LOCAL_DATE, "2019-10-20"));
        builder.setAuthorizationKey("client-key");
        builder.setAuthorization("64x9epryst4b4g2aaks4b3yn");
        builder.setPath("/rest-v1/v1/flights;departureLocation=lhr;arrivalLocation=txl;scheduledDepartureDate=2020-10-20");
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getBBCTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setAuthorization("7f529ec36ab542b78e63f5270a621837");
        builder.setHost("public-api.blablacar.com");
        builder.setPathVariable("api");
        builder.setJourneyPathVariable("trips");
        builder.setApiVersion("v2");
        builder.setProtocol("https");
        builder.setLanguage("de_DE");
        builder.setCurrency("EUR");
        builder.setRadius(30);
        builder.setNumberOfPersons(1);
        builder.setResultLength(100);
        builder.setTimeIsDeparture(true);
        builder.setSortDirection("departure_datetime:desc");
        builder.setDeparture("Berlin");
        builder.setArrival("Hamburg");
        builder.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(52.526455d, 13.367701d).build());
        builder.setArrivalCoordinates(new Coordinates.CoordinatesBuilder(53.553918d, 10.005147d).build());
        builder.setDepartureDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-31 13:00:00"));
        builder.setArrivalDate(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-06-02 13:00:00"));
        return builder.build();
    }

    public static ApiTokenAndUrlInformation getAirportsFinderTokenAndUrl() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol("https");
        builder.setHost("cometari-airportsfinder-v1.p.rapidapi.com");
        builder.setPathVariable("api/airports/by-radius");
        builder.setDepartureCoordinates(new Coordinates.CoordinatesBuilder(48.1301564, 8.2324351).build());
        builder.setAuthorizationKey("x-rapidapi-key");
        builder.setAuthorization("b441403e78mshfe074d6ec0c2af2p1be89cjsn04932ccb889e");
        builder.setPath("/api/airports/by-radius;radius=300;lng=8.2324351;lat=48.1301564");
        return builder.build();
    }
}
