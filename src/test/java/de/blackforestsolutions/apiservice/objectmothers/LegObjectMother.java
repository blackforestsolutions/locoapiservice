package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.*;
import org.springframework.data.geo.Distance;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getVBBPrice;
import static de.blackforestsolutions.apiservice.objectmothers.TravelLineObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother.getGartenstrasseTravelPoint;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;

public class LegObjectMother {

    public static Leg getEiderstrasseRendsburgGartenstrasseRendsburgLeg() {
        Leg.LegBuilder journey = new Leg.LegBuilder(TEST_UUID_2);
        journey.setStart(getRendsburgEiderstrasseTravelPoint());
        journey.setDestination(getGartenstrasseTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123500"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setDistance(new Distance(165));
        journey.setVehicleType(VehicleType.WALK);
        journey.setTravelProvider(TravelProvider.NAHSH);
        return journey.build();
    }

    public static Leg getGartenstrasseRendsburgLeg() {
        Leg.LegBuilder journey = new Leg.LegBuilder(TEST_UUID_3);
        journey.setStart(getGartenstrasseTravelPoint());
        journey.setDestination(getRendsburgZOBTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setProviderId("1|35367|6|80|28032020");
        journey.setTravelLine(getGartenstrasseRendsburgTravelLine());
        journey.setVehicleType(VehicleType.BUS);
        journey.setVehicleName("Bus");
        journey.setVehicleNumber("Bus 11");
        journey.setTravelProvider(TravelProvider.NAHSH);
        return journey.build();
    }

    public static Leg getRendsburgZobToRendsburgLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setId(TEST_UUID_4);
        leg.setStart(getRendsburgZOBTravelPoint());
        leg.setDestination(getRendsburgTravelPoint(""));
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125100"));
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
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
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
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
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setStart(getRendsburgTravelPoint("1"));
        leg.setDestination(getHamburgHbfTravelPoint("12C-F"));
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125600"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "141500"));
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
        leg.setProviderId("1|250984|1|80|28032020");
        leg.setTravelLine(getRendsburgHamburgHbfTravelLine());
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("Regional-Express");
        leg.setVehicleNumber("RE 21071");
        leg.setTravelProvider(TravelProvider.DB);
        return leg.build();
    }

    public static Leg getHamburgHbfFrankfurtHbfLeg() {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_5);
        leg.setStart(getHamburgHbfTravelPoint("14"));
        leg.setDestination(getFrankfurtHbfTravelPoint());
        leg.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "150100"));
        leg.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "190000"));
        leg.setDuration(generateDurationFromStartToDestination(leg.getStartTime(), leg.getArrivalTime()));
        leg.setProviderId("1|245683|0|80|28032020");
        leg.setTravelLine(getHamburgHbfFrankfurtHbfTravelLine());
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("Intercity-Express");
        leg.setVehicleNumber("ICE 771");
        leg.setTravelProvider(TravelProvider.DB);
        return leg.build();
    }

    public static Leg getEinsiedeln_to_WaedenswilLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_2);
        leg.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:25:00"));
        leg.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:50:00"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setStart(getEinsiedelnTravelPoint());
        leg.setDestination(getWaedenswilExitTravelPoint());
        leg.setProviderId("T2019_19353_000082_101_b560270_0");
        leg.setUnknownTravelProvider("SOB-sob");
        leg.setVehicleNumber("S13");
        leg.setVehicleType(VehicleType.TRAIN);
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        Map<Integer, TravelPoint> betweenHolds = new HashMap<>();
        betweenHolds.put(0, getBiberbruggTravelPoint());
        betweenHolds.put(1, getSchindellegiFeusisbergTravelPoint());
        betweenHolds.put(2, getSamstagernTravelPoint());
        travelLine.setBetweenHolds(betweenHolds);
        leg.setTravelLine(travelLine.build());
        return leg.build();
    }

    public static Leg getZuerichHb_to_ZurichSihlauaiLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_3);
        leg.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:17:00"));
        leg.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:24:00"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setStart(getZuerichHbLegTravelPoint());
        leg.setDestination(getZuerichSihlquaiExitTravelPoint());
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getZuerichFoerlibuckstreet_to_ZuerichFoerlibuckstreet60_Leg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:38:00"));
        leg.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:43:00"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setStart(getZuerichLegFoerlibuckstreet());
        leg.setDestination(getZuerichExitFoerlibuckstreet60());
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getGustavHeinemannStreetToHainholzLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_2);
        leg.setStart(getGustavHeinemannStraßeTravelPoint());
        leg.setStartTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:05"));
        leg.setDestination(getHainholzTravelPoint());
        leg.setArrivalTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:13"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getElmshornToHamburgAltonaHbfLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_3);
        leg.setStart(getElmshornTravelPoint());
        leg.setStartTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 13:40"));
        leg.setDestination(getHamburgAltonaBfTravelPoint());
        leg.setArrivalTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:04"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.TRAIN);
        leg.setVehicleName("RB71");
        leg.setProviderId("DB-EFZ:RB71_DB-EFZ_Z");
        leg.setTravelLine(getElmshornHamburgAltonaTravelLine());
        leg.setIncidents(List.of(
                "Maskenpflicht im HVV",
                "Bitte beachten Sie:\nIn allen Verkehrsmitteln des HVV und auf den Haltestellen ist ein Mund-Nasen-Schutz zu tragen.\n"
        ));
        return leg.build();
    }

    public static Leg getHamburgAltonaBfToHamburgAltonaLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_4);
        leg.setStart(getHamburgAltonaBfTravelPoint());
        leg.setStartTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:04"));
        leg.setDestination(getHamburgAltonaTravelPoint());
        leg.setArrivalTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:12"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.WALK);
        return leg.build();
    }

    public static Leg getDammtorToUniversityHamburgLeg() throws ParseException {
        Leg.LegBuilder leg = new Leg.LegBuilder(TEST_UUID_5);
        leg.setStart(getDammtorTravelPoint());
        leg.setStartTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:32"));
        leg.setDestination(getUniversityHamburgTravelPoint());
        leg.setArrivalTime(generateDateFromPatternAndString("dd.MM.yyyy HH:mm", "05.05.2020 14:33"));
        leg.setDuration(buildDurationBetween(
                leg.getStartTime(),
                leg.getArrivalTime()
        ));
        leg.setTravelProvider(TravelProvider.HVV);
        leg.setVehicleType(VehicleType.BUS);
        leg.setVehicleName("Niederflur Metrobus");
        leg.setProviderId("HHA-B:5_HHA-B");
        leg.setTravelLine(getElmshornHamburgAltonaTravelLine());
        leg.setIncidents(List.of(
                "Maskenpflicht im HVV",
                "Bitte beachten Sie:\nIn allen Verkehrsmitteln des HVV und auf den Haltestellen ist ein Mund-Nasen-Schutz zu tragen.\n"
        ));
        return leg.build();
    }
}