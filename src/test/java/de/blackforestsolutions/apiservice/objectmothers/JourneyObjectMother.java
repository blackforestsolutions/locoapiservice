package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.TravelLine;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.TravelProvider;
import org.springframework.data.geo.Distance;

import java.text.ParseException;
import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelLineObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;

public class JourneyObjectMother {

    public static Journey getEiderstrasseRendsburgToRendsburgJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_1);
        journey.setStart(getRendsburgEiderstrasseTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123500"));
        journey.setDestination(getRendsburgTravelPoint(""));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125100"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setPrice(getNahShPrice());
        journey.setBetweenTrips(List.of(
                getEiderstrasseRendsburgGartenstrasseRendsburgTrip(),
                getGartenstrasseRendsburgTrip(),
                getRendsburgZobToRendsburgJourney())
        );
        journey.setTravelProvider(TravelProvider.NAHSH);
        return journey.build();
    }

    private static Journey getRendsburgZobToRendsburgJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_4);
        journey.setStart(getRendsburgZOBTravelPoint());
        journey.setDestination(getRendsburgTravelPoint(""));
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125100"));
        journey.setDistance(new Distance(207));
        journey.setVehicleType("WALK");
        return journey.build();
    }

    public static Journey getPotsdamBerlinJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_1);
        journey.setStart(getPotsdamTravelPoint());
        journey.setDestination(getBerlinTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "122500"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200418", "HHmmss", "124900"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setPrice(getVBBPrice());
        journey.setTravelProvider(TravelProvider.VBB);
        journey.setProviderId("1|642|4|86|18042020");
        journey.setTravelLine(getPotsdamBerlinTravelLine());
        journey.setVehicleType("RE");
        journey.setVehicleName("RE");
        journey.setVehicleNumber("RE1");
        return journey.build();
    }

    public static Journey getEiderstrasseRendsburgToKarlsruheJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_1);
        journey.setStart(getRendsburgEiderstrasseTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123500"));
        journey.setDestination(getKarlsruheHbfTravelPoint());
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "205200"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setPrice(getDBPrice());
        journey.setBetweenTrips(List.of(
                getEiderstrasseRendsburgGartenstrasseRendsburgTrip(),
                getGartenstrasseRendsburgTrip(),
                getRendsburgHamburgTrip(),
                getHamburgHbfFrankfurtHbfTrip()
        ));
        journey.setTravelProvider(TravelProvider.DB);
        return journey.build();
    }

    private static Journey getEiderstrasseRendsburgGartenstrasseRendsburgTrip() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_2);
        journey.setStart(getRendsburgEiderstrasseTravelPoint());
        journey.setDestination(getGartenstrasseTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123500"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        journey.setDistance(new Distance(165));
        journey.setVehicleType("WALK");
        return journey.build();
    }

    private static Journey getGartenstrasseRendsburgTrip() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_3);
        journey.setStart(getGartenstrasseTravelPoint());
        journey.setDestination(getRendsburgZOBTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "123700"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "124600"));
        journey.setProviderId("1|35367|6|80|28032020");
        journey.setTravelLine(getGartenstrasseRendsburgTravelLine());
        journey.setVehicleType("Bus");
        journey.setVehicleName("Bus");
        journey.setVehicleNumber("Bus 11");
        return journey.build();
    }

    private static Journey getRendsburgHamburgTrip() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_4);
        journey.setStart(getRendsburgTravelPoint("1"));
        journey.setDestination(getHamburgHbfTravelPoint("12C-F"));
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "125600"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "141500"));
        journey.setProviderId("1|250984|1|80|28032020");
        journey.setTravelLine(getRendsburgHamburgHbfTravelLine());
        journey.setVehicleType("RE");
        journey.setVehicleName("Regional-Express");
        journey.setVehicleNumber("RE 21071");
        return journey.build();
    }

    private static Journey getHamburgHbfFrankfurtHbfTrip() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(TEST_UUID_5);
        journey.setStart(getHamburgHbfTravelPoint("14"));
        journey.setDestination(getFrankfurtHbfTravelPoint());
        journey.setStartTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "150100"));
        journey.setArrivalTime(generateDateFrom("yyyyMMdd", "20200328", "HHmmss", "190000"));
        journey.setProviderId("1|245683|0|80|28032020");
        journey.setTravelLine(getHamburgHbfFrankfurtHbfTravelLine());
        journey.setVehicleType("ICE");
        journey.setVehicleName("Intercity-Express");
        journey.setVehicleNumber("ICE 771");
        return journey.build();
    }

    public static Journey getRosenhofToHHStadthausbrueckeJourney() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(UUID.fromString("cf1226f5-2047-442f-8fc3-7d0bb512dcbc"));
        journey.setStart(getRosenhofHvvTravelPoint());
        journey.setDestination(getStadthausbrueckeHvvTravelPoint());
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:11"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:23"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setPrice(getHvvPrice());
        journey.setChildPrice(getHvvChildPrice());
        journey.setTravelProvider(TravelProvider.HVV);
        List<Journey> tripsBetween = new ArrayList<>();
        tripsBetween.add(getTripBetweenFromHasselbrookToStadthausbrueckeJourney());
        journey.setBetweenTrips(tripsBetween);
        return journey.build();
    }

    public static Journey getEinsiedeln_to_Zuerich_Foerlibuckstreet60_Journey() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setStart(getEinsiedelnTravelPoint());
        journey.setDestination(getZuerichExitFoerlibuckstreet60());
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:25:00"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:43:00"));
        journey.setDuration(buildDurationBetween(journey.getStartTime(), journey.getArrivalTime()));
        journey.setBetweenTrips(Arrays.asList(
                getEinsiedeln_to_WaedenswilJourney(),
                getZuerichHb_to_ZurichSihlauai(),
                getZuerichFoerlibuckstreet_to_ZuerichFoerlibuckstreet60()
        ));
        return journey.build();
    }

    private static Journey getTripBetweenFromHasselbrookToStadthausbrueckeJourney() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setId(UUID.fromString("21e80139-0335-4e34-8297-a0dab68b407b"));
        journey.setStart(getHasselbrookHvvTravelPoint());
        journey.setDestination(getStadthausbrueckeHvvTravelPoint());
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:11"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm", "2020-01-01 14:23"));
        journey.setDuration(generateDurationFromStartToDestination(journey.getStartTime(), journey.getArrivalTime()));
        journey.setTravelLine(getHvvTravelLine());
        journey.setVehicleType("TRAIN");
        journey.setVehicleName("S-Bahn");
        journey.setVehicleNumber("S1");
        journey.setProviderId("ZVU-DB:S1_ZVU-DB_S-ZVU");
        return journey.build();
    }

    private static Journey getEinsiedeln_to_WaedenswilJourney() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:25:00"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 14:50:00"));
        journey.setDuration(buildDurationBetween(
                journey.getStartTime(),
                journey.getArrivalTime()
        ));
        journey.setStart(getEinsiedelnTravelPoint());
        journey.setDestination(getWaedenswilExitTravelPoint());
        journey.setProviderId("T2019_19353_000082_101_b560270_0");
        journey.setUnknownTravelProvider("SOB-sob");
        journey.setVehicleNumber("S13");
        journey.setVehicleType("S-Bahn");
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        Map<Integer, TravelPoint> betweenHolds = new HashMap<>();
        betweenHolds.put(0, getBiberbruggTravelPoint());
        betweenHolds.put(1, getSchindellegiFeusisbergTravelPoint());
        betweenHolds.put(2, getSamstagernTravelPoint());
        travelLine.setBetweenHolds(betweenHolds);
        journey.setTravelLine(travelLine.build());
        return journey.build();
    }

    private static Journey getZuerichHb_to_ZurichSihlauai() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:17:00"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:24:00"));
        journey.setDuration(buildDurationBetween(
                journey.getStartTime(),
                journey.getArrivalTime()
        ));
        journey.setStart(getZuerichHbLegTravelPoint());
        journey.setDestination(getZuerichSihlquaiExitTravelPoint());
        journey.setVehicleType("Fussweg");
        return journey.build();
    }

    private static Journey getZuerichFoerlibuckstreet_to_ZuerichFoerlibuckstreet60() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder();
        journey.setStartTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:38:00"));
        journey.setArrivalTime(generateDateFromPatternAndString("yyyy-MM-dd HH:mm:ss", "2019-11-04 15:43:00"));
        journey.setDuration(buildDurationBetween(
                journey.getStartTime(),
                journey.getArrivalTime()
        ));
        journey.setStart(getZuerichLegFoerlibuckstreet());
        journey.setDestination(getZuerichExitFoerlibuckstreet60());
        journey.setVehicleType("Fussweg");
        return journey.build();
    }
}
