package de.blackforestsolutions.apiservice.service.searchfilter;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxServiceImpl implements BoxService {

    @Resource(name = "upperBoxLat")
    double upperBoxLat;
    @Resource(name = "upperBoxLat")
    double upperBoxLon;
    @Resource(name = "upperBoxLat")
    double lowerBoxLat;
    @Resource(name = "upperBoxLat")
    double lowerBoxLon;

    @Override
    public boolean checkIfProviderIsInRangeWiths(Coordinates positionCoordinate) {

        double positionCoordinateLat = positionCoordinate.getLatitude();
        double positionCoordinateLon = positionCoordinate.getLongitude();

        return calculateBoxLogic(upperBoxLat, upperBoxLon, lowerBoxLat, lowerBoxLon, positionCoordinateLat, positionCoordinateLon);
    }

    private boolean calculateBoxLogic(double upperBoxLat, double upperBoxLon, double lowerBoxLat, double lowerBoxLon, double positionCoordinateLat, double positionCoordinateLon) {
        if (upperBoxLat >= positionCoordinateLat && lowerBoxLat <= positionCoordinateLat && upperBoxLon >= positionCoordinateLon && lowerBoxLon <= positionCoordinateLon) {
            return true;
        }
        return false;
    }
}
