package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.configuration.CurrencyConfiguration;
import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.datamodel.PriceCategory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

public class PriceObjectMother {

    public static Price getBlaBlaCarPriceFromBerlinFlughafenToHamburgHbf() {
        return new Price.PriceBuilder()
                .setValues(Map.of(PriceCategory.ADULT, new BigDecimal("11.00")))
                .setAffiliateLinks(Map.of(PriceCategory.ADULT, "https://www.blablacar.de/trip/carpooling/1981400891-berlin-hamburg"))
                .setSymbol("€")
                .setCurrency(Currency.getInstance("EUR"))
                .build();
    }

    public static Price getBlaBlaCarPriceFromBerlinHbfToHamburgLandwehr() {
        return new Price.PriceBuilder()
                .setValues(Map.of(PriceCategory.ADULT, new BigDecimal("14.00")))
                .setAffiliateLinks(Map.of(PriceCategory.ADULT, "https://www.blablacar.de/trip/carpooling/1928522132-berlin-hamburg"))
                .setSymbol("€")
                .setCurrency(Currency.getInstance("EUR"))
                .build();
    }


    static Price getRMVPrice() {
        return new Price.PriceBuilder()
                .setValues(Map.of(
                        PriceCategory.ADULT, new BigDecimal("8.70"),
                        PriceCategory.CHILD, new BigDecimal("5.10"),
                        PriceCategory.ADULT_REDUCED, new BigDecimal("6.50"),
                        PriceCategory.CHILD_REDUCED, new BigDecimal("3.80")
                ))
                .setCurrency(Currency.getInstance("EUR"))
                .setSymbol("€")
                .build();
    }

    public static Price getNahShPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal("2.20")));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol(CurrencyConfiguration.EURO);
        return price.build();
    }

    static Price getVBBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal("3.60")));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setSymbol(CurrencyConfiguration.EURO);
        return price.build();
    }

    static Price getHvvPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setValues(Map.of(
                PriceCategory.ADULT, BigDecimal.valueOf(5.4d),
                PriceCategory.ADULT_REDUCED, BigDecimal.valueOf(5.24d),
                PriceCategory.CHILD, BigDecimal.valueOf(2.6d),
                PriceCategory.CHILD_REDUCED, BigDecimal.valueOf(2.52d)
        ));
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setAffiliateLinks(Map.of(
                PriceCategory.ADULT, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte%26bereich%3D3+Ringe%26start%3DElmshorn%252C%2BHainholz%26gueltig%3D2020-05-05%26zones%3DC%2CA",
                PriceCategory.ADULT_REDUCED, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte%26bereich%3D3+Ringe%26start%3DElmshorn%252C%2BHainholz%26gueltig%3D2020-05-05%26zones%3DC%2CA",
                PriceCategory.CHILD, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte+Kind%26bereich%3D5+Ringe%26start%3DElmshorn%252C%2BHainholz%26gueltig%3D2020-05-05%26zones%3DC%2CA",
                PriceCategory.CHILD_REDUCED, "https://shop.hvv.de/index.php/generic/culture/de?return=https%3A%2F%2Fshop.hvv.de%2Findex.php%2Ffahrplanauskunft%3Fkarte%3DEinzelkarte+Kind%26bereich%3D5+Ringe%26start%3DElmshorn%252C%2BHainholz%26gueltig%3D2020-05-05%26zones%3DC%2CA"
        ));
        price.setSymbol(CurrencyConfiguration.EURO);
        return price.build();
    }

    public static Price getDBPrice() {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol(CurrencyConfiguration.EURO);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValues(Map.of(PriceCategory.ADULT, new BigDecimal("65.90")));
        return price.build();
    }
}
