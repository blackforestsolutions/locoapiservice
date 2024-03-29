package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.configuration.CurrencyConfiguration;
import de.blackforestsolutions.apiservice.service.mapper.HafasPriceMapper;
import de.blackforestsolutions.datamodel.*;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.TrfRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class VBBApiServiceImpl implements VBBApiService {

    private static final int FIRST_INDEX = 0;

    private final HafasApiService hafasApiService;

    @Autowired
    public VBBApiServiceImpl(HafasApiService hafasApiService) {
        this.hafasApiService = hafasApiService;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HafasPriceMapper priceMapper = VBBApiServiceImpl::mapPrice;
        return hafasApiService.getJourneysForRouteWith(apiTokenAndUrlInformation, TravelProvider.VBB, priceMapper);
    }

    private static Price mapPrice(TrfRes trfRes) {
        Price.PriceBuilder price = new Price.PriceBuilder();
        price.setSymbol(CurrencyConfiguration.EURO);
        price.setCurrency(Currency.getInstance(Locale.GERMANY));
        price.setValues(convertPriceToPriceWithComma(trfRes.getFareSetL().get(FIRST_INDEX).getFareL().get(FIRST_INDEX).getTicketL().get(FIRST_INDEX).getPrc()));
        return price.build();
    }

    private static Map<PriceCategory, BigDecimal> convertPriceToPriceWithComma(long price) {
        StringBuilder sb = new StringBuilder(String.valueOf(price));
        int length = (int) Math.log10(price) + 1;
        sb.insert(length - 2, ".");
        return Map.of(
                PriceCategory.ADULT, new BigDecimal(sb.toString())
        );
    }

}
