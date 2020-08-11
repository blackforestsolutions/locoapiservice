package de.blackforestsolutions.apiservice.service.searchfilter;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Box;
import org.springframework.stereotype.Service;

@Service
public class BoxServiceImpl implements BoxService {

    private Box boxWithPoints;

    @Autowired
    public BoxServiceImpl(Box boxWithPoints) {
        this.boxWithPoints = boxWithPoints;
    }

    @Override
    public boolean isProviderInRangeWiths(Coordinates positionCoordinate) {

        double positionCoordinateLat = positionCoordinate.getLatitude();
        double positionCoordinateLon = positionCoordinate.getLongitude();
        double upperPointLat = boxWithPoints.getFirst().getX();
        double upperPointLon = boxWithPoints.getFirst().getY();
        double lowerPointLat = boxWithPoints.getSecond().getX();
        double lowerPointLon = boxWithPoints.getSecond().getY();

        return calculateBoxLogic(upperPointLat, upperPointLon, lowerPointLat, lowerPointLon, positionCoordinateLat, positionCoordinateLon);
    }

    private boolean calculateBoxLogic(double upperPointLat, double upperPointLon, double lowerPointLat, double lowerPointLon, double positionCoordinateLat, double positionCoordinateLon) {
        return upperPointLat >= positionCoordinateLat && upperPointLon <= positionCoordinateLon && lowerPointLat <= positionCoordinateLat && lowerPointLon >= positionCoordinateLon;
    }
}
