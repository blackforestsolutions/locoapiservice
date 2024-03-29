package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.Coordinates;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.generatedcontent.hvv.request.Coordinate;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import de.blackforestsolutions.generatedcontent.hvv.request.SDType;

import java.util.List;
import java.util.Locale;

import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFrom;
import static de.blackforestsolutions.apiservice.util.CoordinatesUtil.convertWGS84ToCoordinatesWith;


public class TravelPointObjectMother {

    public static TravelPoint getBerlinHbfTravelPointForBlaBlaCar() {
        return new TravelPoint.TravelPointBuilder()
                .setCity("Berlin")
                .setStationName("Berlin Hauptbahnhof, Berlin")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(52.525083d, 13.369402d).build())
                .setCountry(Locale.GERMAN)
                .build();
    }

    public static TravelPoint getBerlinFlughafenTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setCity("Berlin")
                .setStationName("Flughafen Berlin-Tegel, Berlin")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(52.558832d, 13.288437d).build())
                .setCountry(Locale.GERMAN)
                .build();
    }

    public static TravelPoint getHamburgHbfTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setCity("Hamburg")
                .setStationName("Hamburg Hbf, Hamburg")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.552925d, 10.006604d).build())
                .setCountry(Locale.GERMAN)
                .build();
    }

    public static TravelPoint getHamburgMittlerLandwegTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setCity("Hamburg")
                .setStationName("Mittlerer Landweg, Hamburg")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.495142d, 10.128684d).build())
                .setCountry(Locale.GERMAN)
                .build();
    }

    public static TravelPoint getEuropeTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationName("Europa")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(51d, 10d).build())
                .build();
    }

    public static TravelPoint getStuttgartWaiblingerStreetTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationName("84, Waiblinger Straße, Seelberg, Bad Cannstatt, Stuttgart, Baden-Württemberg, 70372, Deutschland")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(48.80549925, 9.228576954173775).build())
                .setCountry(Locale.GERMAN)
                .setCity("Stuttgart")
                .setStreet("Waiblinger Straße 84")
                .setPostalCode("70372")
                .build();
    }

    public static HvvStation getRosenhofHvvStation() {
        HvvStation travelPoint = new HvvStation();

        travelPoint.setName("Rosenhof");
        travelPoint.setCity("Ahrensburg");
        travelPoint.setCombinedName("Ahrensburg");
        travelPoint.setId("Master:35009");
        travelPoint.setType(SDType.STATION);
        travelPoint.setCoordinate(new Coordinate(9.93454, 53.552405));

        return travelPoint;
    }

    public static HvvStation getStadthausbrueckeHvvStation() {
        HvvStation travelPoint = new HvvStation();

        travelPoint.setName("Stadthausbrücke");
        travelPoint.setCity("Hamburg");
        travelPoint.setCombinedName("Stadthausbrücke");
        travelPoint.setId("Master:11952");
        travelPoint.setType(SDType.STATION);
        travelPoint.setCoordinate(new Coordinate(9.93454, 53.552405));

        return travelPoint;
    }

    public static TravelPoint getBerlinHbfTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setCity("Berlin")
                .setStationName("Berlin Hauptbahnhof, Berlin")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(52.525083d, 13.369402d).build())
                .setCountry(Locale.GERMAN)
                .build();
    }

    public static TravelPoint getRoedemarkOberRodenTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationName("Rödermark-Ober-Roden Bahnhof")
                .build();
    }

    public static TravelPoint getGriesheimbahnhofTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationId("A=1@O=Frankfurt (Main) Griesheim Bahnhof@X=8606425@Y=50093980@U=80@L=3000110@")
                .setStationName("Frankfurt (Main) Griesheim Bahnhof")
                .setArrivalTime(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 09:14:00"))
                .setDepartureTime(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 09:14:00"))
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(50.09398d, 8.606425d).build())
                .setCountry(Locale.GERMANY)
                .build();
    }

    public static TravelPoint getLorchhausenOberfleckenTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationId("A=2@O=Lorch - Lorchhausen, Oberflecken@X=7785108@Y=50053277@U=103@")
                .setStationName("Lorch - Lorchhausen, Oberflecken")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(50.053277d, 7.785108d).build())
                .setCountry(Locale.GERMANY)
                .build();
    }

    public static TravelPoint getWiesbadenHauptbahnhofTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationId("A=1@O=Wiesbaden Hauptbahnhof@X=8242416@Y=50069718@U=80@L=690748@")
                .setStationName("Wiesbaden Hauptbahnhof")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(50.069718d, 8.242416d).build())
                .setCountry(Locale.GERMANY)
                .build();
    }

    public static TravelPoint getWiesbadenHauptbahnhofTravelPoint(String track) {
        return new TravelPoint.TravelPointBuilder()
                .setStationId("A=1@O=Wiesbaden Hauptbahnhof@X=8244627@Y=50069476@U=80@L=3006907@")
                .setStationName("Wiesbaden Hauptbahnhof")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(50.069476d, 8.244627d).build())
                .setPlatform(track)
                .setCountry(Locale.GERMANY)
                .build();
    }

    public static TravelPoint getFrankfurtHauptbahnhofTravelPoint() {
        return new TravelPoint.TravelPointBuilder()
                .setStationId("A=1@O=Frankfurt (Main) Hauptbahnhof tief@X=8662500@Y=50107158@U=80@L=3007010@")
                .setStationName("Frankfurt (Main) Hauptbahnhof tief")
                .setGpsCoordinates(new Coordinates.CoordinatesBuilder(50.107158d, 8.6625d).build())
                .setPlatform("102")
                .setCountry(Locale.GERMANY)
                .build();
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

    public static TravelPoint getGustavHeinemannStrasseTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("9600");
        travelPoint.setStationName("Gustav-Heinemann-Straße, Elmshorn");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.737662, 9.675454).build());
        return travelPoint.build();
    }

    public static TravelPoint getHainholzTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:70096");
        travelPoint.setStationName("Elmshorn, Hainholz");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.738052, 9.670171).build());
        return travelPoint.build();
    }

    public static TravelPoint getHafasElmshornTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Elmshorn");
        travelPoint.setStationId("8000092");
        travelPoint.setGpsCoordinates(convertWGS84ToCoordinatesWith(9659053, 53755116));
        travelPoint.setPlatform("2");
        travelPoint.setDepartureTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "134900"));
        travelPoint.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "134800"));
        return travelPoint.build();
    }


    public static TravelPoint getHvvElmshornTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:97960");
        travelPoint.setStationName("Elmshorn");
        travelPoint.setCity("Elmshorn");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.754662, 9.659292).build());
        travelPoint.setPlatform("Gleis 2");
        return travelPoint.build();
    }

    public static TravelPoint getHamburgAltonaBfTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:8002553");
        travelPoint.setStationName("Hamburg-Altona");
        travelPoint.setCity("Hamburg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.553269, 9.935405).build());
        travelPoint.setPlatform("Gleis 9");
        return travelPoint.build();
    }

    public static TravelPoint getPinnebergTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("Master:99951");
        travelPoint.setStationName("Pinneberg");
        travelPoint.setCity("Pinneberg");
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(53.654943, 9.798367).build());
        travelPoint.setDepartureTime(TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm", "2020-05-05 13:52"));
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
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static TravelPoint getHamburgAltonaDirectionTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hamburg-Altona");
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static TravelPoint getBurgwedelTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setCountry(Locale.GERMANY);
        travelPoint.setStationName("A Burgwedel");
        return travelPoint.build();
    }

    public static TravelPoint getHamburgHauptbahnhofDirectionTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationName("Hauptbahnhof/ZOB");
        travelPoint.setCountry(Locale.GERMANY);
        return travelPoint.build();
    }

    public static List<TravelPoint> getTravelPointsForAirportsFinder() {
        return List.of(
                getZuerichAirportTravelPoint(),
                getBadenBadenTravelPoint()
        );
    }

    public static TravelPoint getZuerichAirportTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("ZRH");
        travelPoint.setStationName("Zürich Airport");
        travelPoint.setCountry(new Locale("", "switzerland"));
        travelPoint.setCity("Zurich");
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(47.464699, 8.54917).build());
        return travelPoint.build();
    }

    public static TravelPoint getBadenBadenTravelPoint() {
        TravelPoint.TravelPointBuilder travelPoint = new TravelPoint.TravelPointBuilder();
        travelPoint.setStationId("FKB");
        travelPoint.setStationName("Karlsruhe Baden-Baden Airport");
        travelPoint.setCity("Karlsruhe/Baden-Baden");
        travelPoint.setCountry(new Locale("", "germany"));
        travelPoint.setGpsCoordinates(new Coordinates.CoordinatesBuilder(48.7793998718, 8.08049964905).build());
        return travelPoint.build();
    }
}
