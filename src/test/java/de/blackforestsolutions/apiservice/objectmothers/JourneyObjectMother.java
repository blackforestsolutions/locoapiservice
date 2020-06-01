package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.TravelProvider;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.LegObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getDBPrice;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getNahShPrice;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;

public class JourneyObjectMother {

    public static Journey getLorchhausenOberfleckenToFrankfurtHauptbahnhofJourney() throws ParseException {
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getLorchhausenOberfleckenToWiesbadenHauptbahnhofLeg().getId(), getLorchhausenOberfleckenToWiesbadenHauptbahnhofLeg());
        legs.put(getWiesbadenHauptbahnhofTransferLeg().getId(), getWiesbadenHauptbahnhofTransferLeg());
        legs.put(getWiesbadenHauptbahnhofToFrankfurtHauptbahnhofLeg().getId(), getWiesbadenHauptbahnhofToFrankfurtHauptbahnhofLeg());
        return new Journey.JourneyBuilder(TEST_UUID_1)
                .setLegs(legs)
                .build();
    }

    public static Journey getEiderstrasseRendsburgToRendsburgJourney() {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getNahShPrice()).getId(), getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getNahShPrice()));
        legs.put(getGartenstrasseRendsburgLeg(TravelProvider.NAHSH).getId(), getGartenstrasseRendsburgLeg(TravelProvider.NAHSH));
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
        legs.put(getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getDBPrice()).getId(), getEiderstrasseRendsburgGartenstrasseRendsburgLeg(getDBPrice()));
        legs.put(getGartenstrasseRendsburgLeg(TravelProvider.DB).getId(), getGartenstrasseRendsburgLeg(TravelProvider.DB));
        legs.put(getRendsburgHamburgLeg().getId(), getRendsburgHamburgLeg());
        legs.put(getHamburgHbfFrankfurtHbfLeg().getId(), getHamburgHbfFrankfurtHbfLeg());
        journey.setLegs(legs);
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

    public static Journey getGustavHeinemannStreetToUniversityJourney() throws ParseException {
        Journey.JourneyBuilder journey = new Journey.JourneyBuilder(TEST_UUID_1);
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getGustavHeinemannStreetToHainholzLeg().getId(), getGustavHeinemannStreetToHainholzLeg());
        legs.put(getElmshornToHamburgAltonaHbfLeg().getId(), getElmshornToHamburgAltonaHbfLeg());
        legs.put(getHamburgAltonaBfToHamburgAltonaLeg().getId(), getHamburgAltonaBfToHamburgAltonaLeg());
        legs.put(getDammtorToUniversityHamburgLeg().getId(), getDammtorToUniversityHamburgLeg());
        journey.setLegs(legs);
        return journey.build();
    }
}
