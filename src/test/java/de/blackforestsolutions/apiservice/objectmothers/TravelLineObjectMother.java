package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.TravelLine;
import de.blackforestsolutions.datamodel.TravelPoint;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.blackforestsolutions.apiservice.objectmothers.TravelpointObjectMother.*;

public class TravelLineObjectMother {

    static TravelLine getPotsdamBerlinTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getBerlinOstkreuzTravelPoint());
        travelLine.setBetweenHolds(Map.of(
                0, getWanseeTravelPoint(),
                1, getCharlottenburgTravelPoint(),
                2, getZoologischerGartenBhfTravelPoint()
        ));
        return travelLine.build();
    }

    static TravelLine getHvvTravelLine() throws ParseException {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setOrigin(getOriginHvvTravelPoint());
        travelLine.setBetweenHolds(getHvvHoldsBetween());
        travelLine.setDirection(getDirectionHvvTravelPoint());
        return travelLine.build();
    }

    private static Map<Integer, TravelPoint> getHvvHoldsBetween() throws ParseException {
        Map<Integer, TravelPoint> betweenHolds = new HashMap<>();
        betweenHolds.put(0, getLandwehrHvvTravelPoint());
        betweenHolds.put(1, getBerlinerTorHvvTravelPoint());
        return betweenHolds;
    }

    static TravelLine getGartenstrasseRendsburgTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getBuedelsdorfStadionTravelPoint());
        travelLine.setBetweenHolds(Collections.singletonMap(0, getMartinshausTravelPoint()));
        return travelLine.build();
    }

    static TravelLine getRendsburgHamburgHbfTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getHamburgHbfTravelPointShort());
        travelLine.setBetweenHolds(Collections.singletonMap(2, getElmshornTravelPoint()));
        return travelLine.build();
    }

    static TravelLine getHamburgHbfFrankfurtHbfTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getStuttgartTravelPoint());
        travelLine.setBetweenHolds(Collections.singletonMap(0, getHannoverTravelPoint()));
        return travelLine.build();
    }

    static TravelLine getElmshornHamburgAltonaTravelLine() throws ParseException {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setOrigin(getWristTravelPoint());
        travelLine.setDirection(getHamburgAltonaDirectionTravelPoint());
        travelLine.setBetweenHolds(Map.of(
                0, getPinnebergTravelPoint()
        ));
        return travelLine.build();
    }

    static TravelLine getHamburgDammtorUniversityTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setOrigin();
        travelLine.setDirection(new TravelPoint.TravelPointBuilder().set);
    }
}
