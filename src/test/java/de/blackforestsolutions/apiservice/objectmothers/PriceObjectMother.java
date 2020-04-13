package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.Price;

import java.util.Currency;
import java.util.Locale;

public class PriceObjectMother {

    static Price getNahShPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValue(2.2d);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol("€");
        return price.build();
    }

    static Price getVBBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValue(3.6d);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol("€");
        return price.build();
    }

    static Price getHvvPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValue(3.4d);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setAffiliateLink("https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte%26bereich%3DHamburg+AB%26start%3DAhrensburg%252C%2BRosenhof%26gueltig%3D2020-01-01");
        price.setSymbol("€");
        return price.build();
    }

    static Price getHvvChildPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValue(1.3d);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setAffiliateLink("https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte+Kind%26bereich%3DHamburg+AB%26start%3DAhrensburg%252C%2BRosenhof%26gueltig%3D2020-01-01");
        price.setSymbol("€");
        return price.build();
    }

    static Price getDBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol("€");
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValue(65.9d);
        return price.build();
    }
}