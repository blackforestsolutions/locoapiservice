package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.util.CoordinatesUtil;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.hvv.request.Coordinate;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import de.blackforestsolutions.generatedcontent.hvv.request.SDType;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFrom;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFromPatternAndString;
import static de.blackforestsolutions.apiservice.util.CoordinatesUtil.convertWGS84ToCoordinatesWith;


public class TravelpointObjectMother {

    private static final Locale SWIZERLAND_LOCALE = Locale.forLanguageTag("de-CH");

    public static HvvStation getRosenhofHvvStation() {
        HvvStation travelPoint = new HvvStation();

        travelPoint.setName("Rosenhof");
        travelPoint.setCity("Ahrensburg");
        travelPoint.setCombinedName("Ahrensburg");
        travelPoint.setId("Master:35009");
        travelPoint.setType(SDType.STATION);
        travelPoint.setCoordinate(
                new Coordinate(
                        9.93454,
                        53.552405
                )
        );

        return travelPoint;
    }

    public static HvvStation getStadthausbrueckeHvvStation() {
        HvvStation travelPoint = new HvvStation();

        travelPoint.setName("Stadthausbrücke");
        travelPoint.setCity("Hamburg");
        travelPoint.setCombinedName("Stadthausbrücke");
        travelPoint.setId("Master:11952");
        travelPoint.setType(SDType.STATION);

        travelPoint.setCoordinate(
                new Coordinate(
                        9.93454,
                        53.552405
                )
        );

        return travelPoint;
    }


    public static TravelPoint getPotsdamTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S Potsdam Hauptbahnhof");
        travelPoint.setStationId("900230999");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(13067187, 52390931));
        travelPoint.setPlatform("4");
        return travelPoint.build();
    }

    public static TravelPoint getBerlinTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S+U Berlin Hauptbahnhof");
        travelPoint.setStationId("900003201");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(13368928, 52525850));
        travelPoint.setPlatform("12");
        return travelPoint.build();
    }

    public static TravelPoint getWanseeTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S Wannsee Bhf (Berlin)");
        travelPoint.setStationId("900053301");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(13179103, 52421458));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "123100"));
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "123200"));
        return travelPoint.build();
    }

    public static TravelPoint getCharlottenburgTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S Charlottenburg Bhf (Berlin)");
        travelPoint.setStationId("900024101");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(13303846, 52504806));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124000"));
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124100"));
        return travelPoint.build();
    }

    public static TravelPoint getZoologischerGartenBhfTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S+U Zoologischer Garten Bhf (Berlin)");
        travelPoint.setStationId("900023201");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(13332711, 52506919));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124400"));
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124500"));
        return travelPoint.build();
    }

    public static TravelPoint getBerlinOstkreuzTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("S Ostkreuz Bhf (Berlin)");
        return travelPoint.build();
    }

    public static TravelPoint getRendsburgEiderstrasseTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Rendsburg, Eiderstraße");
        travelPoint.setStationId("981067408");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9651304, 54293246));
        return travelPoint.build();
    }

    public static TravelPoint getKarlsruheHbfTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Karlsruhe Hbf");
        travelPoint.setStationId("8000191");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(8401939, 48993530));
        travelPoint.setPlatform("5");
        return travelPoint.build();
    }

    public static TravelPoint getGartenstrasseTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Gartenstraße, Rendsburg");
        travelPoint.setStationId("699076");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9652743, 54292581));
        return travelPoint.build();
    }

    public static TravelPoint getRendsburgZOBTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Bahnhofstraße/ZOB, Rendsburg");
        travelPoint.setStationId("698776");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9668132, 54302883));
        return travelPoint.build();
    }

    public static TravelPoint getBuedelsdorfStadionTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Stadion, Büdelsdorf");
        return travelPoint.build();
    }

    public static TravelPoint getMartinshausTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Martinshaus, Rendsburg");
        travelPoint.setStationId("699086");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9653489, 54289686));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123800"));
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123800"));
        return travelPoint.build();
    }

    public static TravelPoint getRendsburgTravelPoint(String platform) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Rendsburg");
        travelPoint.setStationId("8000312");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9670577, 54302712));
        travelPoint.setPlatform(platform);
        return travelPoint.build();
    }

    public static TravelPoint getHamburgHbfTravelPoint(String platform) {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hamburg Hbf");
        travelPoint.setStationId("8002549");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(10006360, 53553533));
        travelPoint.setPlatform(platform);
        return travelPoint.build();
    }

    public static TravelPoint getHamburgHbfTravelPointShort() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hamburg Hbf");
        return travelPoint.build();
    }

    public static TravelPoint getFrankfurtHbfTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Frankfurt(Main)Hbf");
        travelPoint.setStationId("8000105");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(8663003, 50106817));
        travelPoint.setPlatform("6");
        return travelPoint.build();
    }

    public static TravelPoint getStuttgartTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Stuttgart Hbf");
        return travelPoint.build();
    }

    public static TravelPoint getHannoverTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hannover Hbf");
        travelPoint.setStationId("8000152");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9741763, 52377079));
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "164100"));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "163400"));
        return travelPoint.build();
    }

    static TravelPoint getLandwehrHvvTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:10953");
        travelPoint.setStationName("Landwehr");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder(53.561282, 10.037905);
        travelPoint.setGpsCoordinates(coordinates.build());
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:13"));
        return travelPoint.build();
    }

    public static TravelPoint getRosenhofHvvTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:35009");
        travelPoint.setStationName("Ahrensburg, Rosenhof");
        travelPoint.setCity("Ahrensburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLongitude(10.240903);
        coordinates.setLatitude(53.68308);
        travelPoint.setGpsCoordinates(coordinates.build());
        return travelPoint.build();
    }

    public static TravelPoint getStadthausbrueckeHvvTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:11952");
        travelPoint.setStationName("Stadthausbrücke");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLongitude(9.984914);
        coordinates.setLatitude(53.549465);
        travelPoint.setGpsCoordinates(coordinates.build());
        travelPoint.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:23"));

        return travelPoint.build();
    }

    public static TravelPoint getPinnebergRichardKoehnHvvTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();

        travelPoint.setStationId("Master:99969");
        travelPoint.setStationName("Richard-Köhn-Straße/Jahnhalle");
        travelPoint.setCity("Pinneberg");
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static TravelPoint getHasselbrookHvvTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();

        travelPoint.setStationId("Master:60950");
        travelPoint.setStationName("Hasselbrook");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLongitude(10.05656);
        coordinates.setLatitude(53.564947);
        travelPoint.setGpsCoordinates(coordinates.build());
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:11"));
        travelPoint.setPlatform("Gleis 1");

        return travelPoint.build();
    }

    public static TravelPoint getHvvHauptbahnhofTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();

        travelPoint.setStationId("Master:9910910");
        travelPoint.setStationName("Hauptbahnhof");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLatitude(32.3432);
        coordinates.setLongitude(53.34432432);
        travelPoint.setGpsCoordinates(coordinates.build());

        return travelPoint.build();
    }

    public static TravelPoint getOriginHvvTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Poppenbüttel / Hamburg Airport (Flughafen)");
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static TravelPoint getDirectionHvvTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Wedel");
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static TravelPoint getEinsiedelnTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Einsiedeln");
        travelPoint.setStationId("8503283");
        travelPoint.setTerminal("Wädenswil");
        travelPoint.setPlatform("4");
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(699075, 220557));
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        return travelPoint.build();
    }

    public static TravelPoint getWaedenswilExitTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Wädenswil");
        travelPoint.setStationId("8503206");
        travelPoint.setPlatform("1");
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(693643, 231668));
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        return travelPoint.build();
    }

    public static TravelPoint getZuerichHbLegTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Zürich HB");
        travelPoint.setStationId("8503000");
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(683212, 248030));
        return travelPoint.build();
    }

    public static TravelPoint getZuerichSihlquaiExitTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Zürich, Sihlquai/HB");
        travelPoint.setStationId("8591368");
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(682990, 248252));
        return travelPoint.build();
    }

    public static TravelPoint getZuerichLegFoerlibuckstreet() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Zürich, Förrlibuckstrasse");
        travelPoint.setStationId("8591135");
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(681427, 249602));
        return travelPoint.build();
    }

    public static TravelPoint getZuerichExitFoerlibuckstreet60() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Zürich, Förrlibuckstr. 60");
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        return travelPoint.build();
    }

    public static TravelPoint getBiberbruggTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Biberbrugg");
        travelPoint.setStationId("8503284");
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(697373, 223619));
        travelPoint.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:32:00"));
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:32:00"));
        return travelPoint.build();
    }

    public static TravelPoint getGustavHeinemannStraßeTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("9600");
        travelPoint.setStationName("Gustav-Heinemann-Straße");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.737662, 9.675454).build());
        return travelPoint.build();
    }

    public static TravelPoint getHainholzTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:70096");
        travelPoint.setStationName("Hainholz");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.738052,9.670171).build());
        return travelPoint.build();
    }

    public static TravelPoint getElmshornTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:97960");
        travelPoint.setStationName("Elmshorn");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.754662,9.659292).build());
        travelPoint.setPlatform("Gleis 2");
        return travelPoint.build();
    }

    public static TravelPoint getHamburgAltonaBfTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:8002553");
        travelPoint.setStationName("Hamburg-Altona");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.553269,9.935405).build());
        travelPoint.setPlatform("Gleis 9");
        return travelPoint.build();
    }

    public static TravelPoint getPinnebergTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:99951");
        travelPoint.setStationName("Pinneberg");
        travelPoint.setCity("Pinneberg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.654943,9.798367).build());
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-05-05 13:52"));
        return travelPoint.build();
    }

    public static TravelPoint getHamburgAltonaTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:80953");
        travelPoint.setStationName("Altona");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.551992, 9.934365).build());
        travelPoint.setPlatform("Gleis 2");
        return travelPoint.build();
    }

    public static TravelPoint getDammtorTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:11022");
        travelPoint.setStationName("Bf. Dammtor");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.561066, 9.990315).build());
        return travelPoint.build();
    }

    public static TravelPoint getUniversityHamburgTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:11021");
        travelPoint.setStationName("Universität/Staatsbibliothek");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.564242, 9.984442).build());
        return travelPoint.build();
    }

    public static TravelPoint getWristTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Wrist");
        return travelPoint.build();
    }

    public static TravelPoint getHamburgAltonaDirectionTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hamburg-Altona");
        return travelPoint.build();
    }

    public static TravelPoint getBurgwedelTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("A Burgwedel");
        return travelPoint.build();
    }

    public static TravelPoint getHamburgHauptbahnhofDirectionTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hauptbahnhof/ZOB");
        return travelPoint.build();
    }



    public static TravelPoint getBerlinerTorHvvTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:10952");
        travelPoint.setStationName("Berliner Tor");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLongitude(10.024621);
        coordinates.setLatitude(53.553031);
        travelPoint.setGpsCoordinates(coordinates.build());
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:15"));
        return travelPoint.build();
    }

    public static TravelPoint getSchindellegiFeusisbergTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Schindellegi-Feusisberg");
        travelPoint.setStationId("8503285");
        travelPoint.setCountry(SWIZERLAND_LOCALE);
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(696326, 225829));
        travelPoint.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:36:00"));
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:36:00"));
        return travelPoint.build();
    }

    public static TravelPoint getSamstagernTravelPoint() throws ParseException {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Samstagern");
        travelPoint.setStationId("8503286");
        travelPoint.setGpsCoordinates(CoordinatesUtil.convertCh1903ToCoordinatesWith(694526, 227483));
        travelPoint.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:40:00"));
        travelPoint.setDepartureTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:41:00"));
        return travelPoint.build();
    }
}
