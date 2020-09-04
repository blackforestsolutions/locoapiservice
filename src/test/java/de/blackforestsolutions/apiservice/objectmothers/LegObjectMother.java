package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.testutils.TestUtils;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.datamodel.TravelProvider;
import de.blackforestsolutions.datamodel.VehicleType;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelLineObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.generateDateFrom;

public class LegObjectMother {

    public static Leg getFlughafenBerlinToHamburgHbfLeg() {
        ZonedDateTime startTime = LocalDateTime.parse("30/04/2021 08:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.systemDefault());
        ZonedDateTime arrivalTime = LocalDateTime.parse("30/04/2021 08:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.systemDefault()).plusSeconds(10775);
        return new Leg.LegBuilder(TEST_UUID_2)
                .setStartTime(startTime)
                .setStart(getBerlinFlughafenTravelPoint())
                .setDestination(getHamburgHbfTravelPoint())
                .setPrice(getBlaBlaCarPriceFromBerlinFlughafenToHamburgHbf())
                .setHasPrice(true)
                .setArrivalTime(arrivalTime)
                .setDuration(Duration.between(startTime, arrivalTime))
                .setDistance(new Distance(276, Metrics.KILOMETERS))
                .setVehicleName("TESLA MODEL X")
                .setVehicleType(VehicleType.CAR)
                .build();
    }

    public static Leg getBerlinHbfHamburgMittlerLandwegLeg() {
        ZonedDateTime startTime = LocalDateTime.parse("25/10/2020 17:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.systemDefault());
        ZonedDateTime arrivalTime = LocalDateTime.parse("25/10/2020 17:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.systemDefault()).plusSeconds(11130);
        return new Leg.LegBuilder(TEST_UUID_4)
                .setStartTime(startTime)
                .setStart(getBerlinHbfTravelPointForBlaBlaCar())
                .setDestination(getHamburgMittlerLandwegTravelPoint())
                .setPrice(getBlaBlaCarPriceFromBerlinHbfToHamburgLandwehr())
                .setHasPrice(true)
                .setArrivalTime(arrivalTime)
                .setDuration(Duration.between(startTime, arrivalTime))
                .setDistance(new Distance(281, Metrics.KILOMETERS))
                .setVehicleType(VehicleType.CAR)
                .build();
    }

    public static Leg getLorchhausenOberfleckenToWiesbadenHauptbahnhofLeg() {
        ZonedDateTime startTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 07:43:00");
        ZonedDateTime arrivalTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 08:35:00");
        return new Leg.LegBuilder(TEST_UUID_2)
                .setStart(getLorchhausenOberfleckenTravelPoint())
                .setStartTime(startTime)
                .setDestination(getWiesbadenHauptbahnhofTravelPoint())
                .setArrivalTime(arrivalTime)
                .setDistance(new Distance(46423d))
                .setDuration(Duration.between(startTime, arrivalTime))
                .setPrice(getRMVPrice())
                .setVehicleType(VehicleType.CAR)
                .setTravelProvider(TravelProvider.RMV)
                .setHasPrice(true)
                .build();
    }

    public static Leg getWiesbadenHauptbahnhofTransferLeg() {
        ZonedDateTime startTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 08:35:00");
        ZonedDateTime arrivalTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 08:35:00");
        return new Leg.LegBuilder(TEST_UUID_3)
                .setStart(getWiesbadenHauptbahnhofTravelPoint())
                .setStartTime(startTime)
                .setDestination(getWiesbadenHauptbahnhofTravelPoint(""))
                .setArrivalTime(arrivalTime)
                .setDistance(new Distance(160))
                .setDuration(Duration.between(startTime, arrivalTime))
                .setVehicleType(VehicleType.WALK)
                .setTravelProvider(TravelProvider.RMV)
                .build();
    }

    public static Leg getWiesbadenHauptbahnhofToFrankfurtHauptbahnhofLeg() {
        ZonedDateTime startTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 08:35:00");
        ZonedDateTime arrivalTime = TestUtils.generateDateFromLocalDateTimeAndString("yyyy-MM-dd HH:mm:ss", "2020-05-04 09:18:00");
        return new Leg.LegBuilder(TEST_UUID_4)
                .setStart(getWiesbadenHauptbahnhofTravelPoint("4"))
                .setStartTime(startTime)
                .setDestination(getFrankfurtHauptbahnhofTravelPoint())
                .setArrivalTime(arrivalTime)
                .setDuration(Duration.between(startTime, arrivalTime))
                .setTravelLine(getWiesbadenHauptbahnhofFrankfurtHauptbahnhofTravelLine())
                .setVehicleName("09:18:00")
                .setVehicleNumber("S1")
                .setIncidents(List.of(
                        "Pflicht zur Bedeckung von Mund und Nase",
                        "Fahrzeuggebundene Einstiegshilfe vorhanden",
                        "Klimaanlage"
                ))
                .setTravelProvider(TravelProvider.RMV)
                .setUnknownTravelProvider("DB Regio AG S-Bahn Rhein-Main")
                .build();
    }

    public static Leg getEiderstrasseRendsburgGartenstrasseRendsburgLeg(Price price) {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_2);
        leg.setStart(getRendsburgEiderstrasseTravelPoint());
        leg.setDestination(getGartenstrasseTravelPoint());
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123500"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setDistance(new Distance(165));
        leg.setVehicleType(VehicleType.WALK);
        leg.setPrice(price);
        return leg.build();
    }

    public static Leg getGartenstrasseRendsburgLeg(TravelProvider travelProvider) {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_3);
        leg.setStart(getGartenstrasseTravelPoint());
        leg.setDestination(getRendsburgZOBTravelPoint());
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setProviderId("1|35367|6|80|28032020");
        leg.setTravelLine(getGartenstrasseRendsburgTravelLine());
        leg.setVehicleType(VehicleType.BUS);
        leg.setVehicleName("Bus");
        leg.setVehicleNumber("Bus 11");
        leg.setTravelProvider(travelProvider);
        return leg.build();
    }

    public static Leg getRendsburgZobToRendsburgLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setId(TEST_UUID_4);
        leg.setStart(getRendsburgZOBTravelPoint());
        leg.setDestination(getRendsburgTravelPoint(""));
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125100"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setDistance(new Distance(207));
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getPotsdamBerlinLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_2);
        leg.setStart(getPotsdamTravelPoint());
        leg.setDestination(getBerlinTravelPoint());
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "122500"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124900"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setPrice(getVBBPrice());
        leg.setTravelProvider(TravelProvider.VBB);
        leg.setProviderId("1|642|4|86|18042020");
        leg.setTravelLine(getPotsdamBerlinTravelLine());
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("RE");
        leg.setVehicleNumber("RE1");
        return leg.build();
    }

    public static Leg getRendsburgHamburgLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_5);
        leg.setStart(getRendsburgTravelPoint("1"));
        leg.setDestination(getHamburgHbfTravelPoint("12C-F"));
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125600"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "141500"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setProviderId("1|250984|1|80|28032020");
        leg.setTravelLine(getRendsburgHamburgHbfTravelLine());
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("Regional-Express");
        leg.setVehicleNumber("RE 21071");
        leg.setTravelProvider(TravelProvider.DB);
        return leg.build();
    }

    public static Leg getHamburgHbfFrankfurtHbfLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_6);
        leg.setStart(getHamburgHbfTravelPoint("14"));
        leg.setDestination(getFrankfurtHbfTravelPoint());
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "150100"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "190000"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setProviderId("1|245683|0|80|28032020");
        leg.setTravelLine(getHamburgHbfFrankfurtHbfTravelLine());
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("Intercity-Express");
        leg.setVehicleNumber("ICE 771");
        leg.setTravelProvider(TravelProvider.DB);
        return leg.build();
    }

    public static Leg getGustavHeinemannStreetToHainholzLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_2);
        leg.setStart(getGustavHeinemannStrasseTravelPoint());
        leg.setStartTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:05"));
        leg.setDestination(getHainholzTravelPoint());
        leg.setArrivalTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:13"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.WALK);
        leg.setVehicleName("Fußweg");
        leg.setPrice(getHvvPrice());
        return leg.build();
    }

    public static Leg getElmshornToHamburgAltonaHbfLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_3);
        leg.setStart(getHvvElmshornTravelPoint());
        leg.setStartTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:40"));
        leg.setDestination(getHamburgAltonaBfTravelPoint());
        leg.setArrivalTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:04"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("RB71");
        leg.setProviderId("DB-EFZ:RB71_DB-EFZ_Z");
        leg.setTravelLine(getElmshornHamburgAltonaTravelLine());
        leg.setIncidents(List.of(
                "Bitte beachten Sie:\nIn allen Verkehrsmitteln des HVV und auf den Haltestellen ist ein Mund-Nasen-Schutz zu tragen.\n"
        ));
        return leg.build();
    }

    public static Leg getHamburgAltonaBfToHamburgAltonaLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setStart(getHamburgAltonaBfTravelPoint());
        leg.setStartTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:04"));
        leg.setDestination(getHamburgAltonaTravelPoint());
        leg.setArrivalTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:12"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleName("Umstiegsfußweg");
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getDammtorToUniversityHamburgLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_5);
        leg.setStart(getDammtorTravelPoint());
        leg.setStartTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:32"));
        leg.setDestination(getUniversityHamburgTravelPoint());
        leg.setArrivalTime(TestUtils.generateDateFromLocalDateTimeAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:33"));
        leg.setDuration(Duration.between(leg.getStartTime(), leg.getArrivalTime()));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.BUS);
        leg.setVehicleName("5");
        leg.setProviderId("HHA-B:5_HHA-B");
        leg.setTravelLine(getHamburgDammtorUniversityTravelLine());
        leg.setIncidents(List.of(
                "Bitte beachten Sie:\nIn allen Verkehrsmitteln des HVV und auf den Haltestellen ist ein Mund-Nasen-Schutz zu tragen.\n"
        ));
        return leg.build();
    }
}
