package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.Price;
import de.blackforestsolutions.generatedcontent.hafas.response.journey.TrfRes;

@FunctionalInterface
public interface HafasPriceMapper {

    /**
     * Individual method for mapping price in HafasMapperServiceImpl
     * Method needs to be implemented into every ApiService related to Hafas
     *
     * @param price standard Hafas price information object
     * @return Blackforestsolution Price
     */
    Price map(TrfRes price);
}
