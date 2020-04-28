package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.datamodel.PriceCategory;

import java.math.BigDecimal;
import java.util.*;

public class PriceObjectMother {

    public static Price getNahShPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal(2.2d)));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol("€");
        return price.build();
    }

    static Price getVBBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal(3.6d)));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol("€");
        return price.build();
    }

    static Price getHvvPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(
                PriceCategory.ADULT, new BigDecimal(3.4d),
                PriceCategory.CHILD, new BigDecimal(1.3d)
        ));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setAffiliateLinks(Map.of(
                PriceCategory.ADULT, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte%26bereich%3DHamburg+AB%26start%3DAhrensburg%252C%2BRosenhof%26gueltig%3D2020-01-01",
                PriceCategory.CHILD, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte+Kind%26bereich%3DHamburg+AB%26start%3DAhrensburg%252C%2BRosenhof%26gueltig%3D2020-01-01"
        ));
        price.setSymbol("€");
        return price.build();
    }

    public static Price getDBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol("€");
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal(65.9d)));
        return price.build();
    }
}
