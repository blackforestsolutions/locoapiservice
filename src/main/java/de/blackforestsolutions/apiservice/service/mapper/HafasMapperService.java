package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.JourneyStatus;
import de.blackforestsolutions.datamodel.TravelProvider;

import java.util.Map;
import java.util.UUID;

public interface HafasMapperService {

    Map<UUID, JourneyStatus> getJourneysFrom(String body, TravelProvider travelProvider, HafasPriceMapper priceMapper);

    CallStatus getIdFrom(String resultBody);
}
