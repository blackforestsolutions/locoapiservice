package de.blackforestsolutions.apiservice.service.searchfilter;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.data.geo.Box;

public class BoxServiceImpl {

    public boolean checkIfProviderIsInRangeWith(Coordinates coordinate, Box box) {

        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();

        box


    }
}
