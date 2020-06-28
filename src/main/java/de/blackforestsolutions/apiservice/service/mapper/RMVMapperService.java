package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.datamodel.JourneyStatus;

import javax.xml.bind.JAXBException;
import java.util.Map;
import java.util.UUID;

public interface RMVMapperService {
    String getIdFrom(String resultBody) throws JAXBException;

    Map<UUID, JourneyStatus> getJourneysFrom(String resultBody) throws JAXBException;
}
