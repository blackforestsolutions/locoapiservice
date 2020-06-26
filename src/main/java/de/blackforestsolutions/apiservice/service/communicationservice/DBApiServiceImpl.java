package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.mapper.HafasPriceMapper;
import de.blackforestsolutions.apiservice.service.mapper.MapperService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DBApiServiceImpl implements DBApiService {

    private final HafasApiService hafasApiService;

    @Autowired
    public DBApiServiceImpl(HafasApiService hafasApiService) {
        this.hafasApiService = hafasApiService;
    }

    @Override
    public CallStatus<Map<UUID, JourneyStatus>> getJourneysForRouteWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HafasPriceMapper priceMapper = MapperService::mapPriceForHafas;
        return hafasApiService.getJourneysForRouteWith(apiTokenAndUrlInformation, TravelProvider.DB, priceMapper);
    }
}
