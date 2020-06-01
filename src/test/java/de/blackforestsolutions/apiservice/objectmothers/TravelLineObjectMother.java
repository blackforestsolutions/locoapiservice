package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.TravelLine;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.*;

public class TravelLineObjectMother {

    static TravelLine getWiesbadenHauptbahnhofFrankfurtHauptbahnhofTravelLine() throws ParseException {
        return new TravelLine.TravelLineBuilder()
                .setDirection(getRödemarkOberRodenTravelPoint())
                .setBetweenHolds(Map.of(
                        0, getGriesheimbahnhofTravelPoint()
                ))
                .build();
    }

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

    static TravelLine getGartenstrasseRendsburgTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getBuedelsdorfStadionTravelPoint());
        travelLine.setBetweenHolds(Collections.singletonMap(0, getMartinshausTravelPoint()));
        return travelLine.build();
    }

    static TravelLine getRendsburgHamburgHbfTravelLine() {
        TravelLine.TravelLineBuilder travelLine = new TravelLine.TravelLineBuilder();
        travelLine.setDirection(getHamburgHbfTravelPointShort());
        travelLine.setBetweenHolds(Collections.singletonMap(2, getHafasElmshornTravelPoint()));
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
        travelLine.setOrigin(getHamburgHauptbahnhofDirectionTravelPoint());
        travelLine.setDirection(getBurgwedelTravelPoint());
        return travelLine.build();
    }
}
