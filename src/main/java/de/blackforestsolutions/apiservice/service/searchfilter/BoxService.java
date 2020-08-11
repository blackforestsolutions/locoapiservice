package de.blackforestsolutions.apiservice.service.searchfilter;

import de.blackforestsolutions.datamodel.Coordinates;

public interface BoxService {
    boolean isProviderInRangeWiths(Coordinates positionCoordinate);
}
