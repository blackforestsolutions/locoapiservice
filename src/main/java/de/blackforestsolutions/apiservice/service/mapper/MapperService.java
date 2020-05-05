package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.configuration.CurrencyConfiguration;
import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.datamodel.PriceCategory;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.TrfRes;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class MapperService {

    private static final int FIRST_INDEX = 0;

    static String checkIfStringPropertyExists(String jsonProperty) {
        Optional<String> optionalJsonProperty = Optional.ofNullable(jsonProperty);
        return optionalJsonProperty.orElse("");
    }

    static Duration generateDurationFromStartToDestination(Date start, Date destination) {
        return Duration.between(LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(destination.toInstant(), ZoneId.systemDefault()));
    }

    public static Price mapPriceForHafas(TrfRes trfRes) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol(CurrencyConfiguration.EURO);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValues(convertPriceToPriceWithComma(trfRes.getFareSetL().get(FIRST_INDEX).getFareL().get(FIRST_INDEX).getPrc()));
        return price.build();
    }

    private static Map<PriceCategory, BigDecimal> convertPriceToPriceWithComma(int price) {
        StringBuilder sb = new StringBuilder(String.valueOf(price));
        int length = (int) Math.log10(price) + 1;
        sb.insert(length - 2, ".");
        return Map.of(
                PriceCategory.ADULT, new BigDecimal(sb.toString())
        );
    }
}
