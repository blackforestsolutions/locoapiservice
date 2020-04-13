package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.TrfRes;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

public class MapperService {

    private static final int FIRST_INDEX = 0;

    static String checkIfStringPropertyExists(String jsonProperty) {
        Optional<String> optionalJsonProperty = Optional.ofNullable(jsonProperty);
        return optionalJsonProperty.orElse("");
    }

    public static Price mapPriceForHafas(TrfRes trfRes) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol("€");
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValue(convertPriceToPriceWithComma(trfRes.getFareSetL().get(FIRST_INDEX).getFareL().get(FIRST_INDEX).getPrc()));
        return price.build();
    }

    private static double convertPriceToPriceWithComma(int price) {
        StringBuilder sb = new StringBuilder(String.valueOf(price));
        int length = (int) Math.log10(price) + 1;
        sb.insert(length - 2, ".");
        return Double.parseDouble(sb.toString());
    }
}
