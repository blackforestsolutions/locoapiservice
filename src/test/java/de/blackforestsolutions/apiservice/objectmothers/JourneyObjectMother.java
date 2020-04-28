package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.*;

import java.text.ParseException;
import java.util.*;

import static de.blackforestsolutions.apiservice.objectmothers.LegObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelLineObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;

public class JourneyObjectMother {

    public static Journey getEiderstrasseRendsburgToRendsburgJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getNahShPrice()).getId(), getEiderstrasseRendsburgGartenstrasseRendsburgLeg());
        legs.put(getGartenstrasseRendsburgLeg().getId(), getGartenstrasseRendsburgLeg());
        legs.put(getRendsburgZobToRendsburgLeg().getId(), getRendsburgZobToRendsburgLeg());
        journey.setLegs(legs);
        return journey.build();
    }

    public static Journey getPotsdamBerlinJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getPotsdamBerlinLeg().getId(), getPotsdamBerlinLeg());
        journey.setLegs(legs);
        return journey.build();
    }

    public static Journey getEiderstrasseRendsburgToKarlsruheJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getEiderstrasseRendsburgGartenstrasseRendsburgLeg().getId(), getEiderstrasseRendsburgGartenstrasseRendsburgLeg());
        legs.put(getGartenstrasseRendsburgLeg().getId(), getGartenstrasseRendsburgLeg());
        legs.put(getRendsburgHamburgLeg().getId(), getRendsburgHamburgLeg());
        legs.put(getHamburgHbfFrankfurtHbfLeg().getId(), getHamburgHbfFrankfurtHbfLeg());
        journey.setLegs(legs);
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
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getEinsiedeln_to_WaedenswilLeg().getId(), getEinsiedeln_to_WaedenswilLeg());
        legs.put(getZuerichHb_to_ZurichSihlauaiLeg().getId(), getZuerichHb_to_ZurichSihlauaiLeg());
        legs.put(getZuerichFoerlibuckstreet_to_ZuerichFoerlibuckstreet60_Leg().getId(), getZuerichFoerlibuckstreet_to_ZuerichFoerlibuckstreet60_Leg());
        journey.setLegs(legs);
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
}
