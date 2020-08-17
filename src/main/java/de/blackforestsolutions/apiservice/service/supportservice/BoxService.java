package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.data.geo.Box;

public interface BoxService {
    boolean isCoordinateInBox(Coordinates positionCoordinate, Box providerBox);
}
