package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Leg;
import de.blackforestsolutions.datamodel.TravelProvider;

import java.util.LinkedHashMap;
import java.util.UUID;

import static de.blackforestsolutions.apiservice.objectmothers.LegObjectMother.*;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getDBPrice;
import static de.blackforestsolutions.apiservice.objectmothers.PriceObjectMother.getNahShPrice;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.TEST_UUID_3;

public class JourneyObjectMother {

    public static Journey getJourneyWithEmptyFields(UUID id) {
        return new Journey.JourneyBuilder(id)
                .build();
    }

    public static Journey getFlughafenBerlinToHamburgHbfJourney() {
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getFlughafenBerlinToHamburgHbfLeg().getId(), getFlughafenBerlinToHamburgHbfLeg());
        return new Journey.JourneyBuilder(TEST_UUID_1)
                .setLegs(legs)
                .build();
    }

    public static Journey getBerlinHbfToHamburgLandwehrJourney() {
        LinkedHashMap<UUID, Leg> legs = new LinkedHashMap<>();
        legs.put(getBerlinHbfHamburgMittlerLandwegLeg().getId(), getBerlinHbfHamburgMittlerLandwegLeg());
        return new Journey.JourneyBuilder(TEST_UUID_3)
                .setLegs(legs)
                .build();
    }

    public static Journey getLorchhausenOberfleckenToFrankfurtHauptbahnhofJourney() {
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

    public static Journey getGustavHeinemannStreetToUniversityJourney() {
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
