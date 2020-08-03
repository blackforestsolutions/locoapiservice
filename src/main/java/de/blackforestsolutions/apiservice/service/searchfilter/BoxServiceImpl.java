package de.blackforestsolutions.apiservice.service.searchfilter;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxServiceImpl implements BoxService {

    //@Resource(name = "boxWithPoints")
    private Box boxWithPoints;

    @Autowired
    public BoxServiceImpl(Box boxWithPoints) {
        this.boxWithPoints = boxWithPoints;
    }

    @Override
    public boolean checkIfProviderIsInRangeWiths(Coordinates positionCoordinate) {

        double positionCoordinateLat = positionCoordinate.getLatitude();
        double positionCoordinateLon = positionCoordinate.getLongitude();

        Point firstPoint = this.boxWithPoints.getFirst();
        Point secondPoint = this.boxWithPoints.getSecond();

        double upperBoxLat = boxWithPoints.getFirst().getX();
        double upperBoxLon = boxWithPoints.getFirst().getY();
        double lowerBoxLat = boxWithPoints.getSecond().getX();
        double lowerBoxLon = boxWithPoints.getSecond().getY();

        return calculateBoxLogic(upperBoxLat, upperBoxLon, lowerBoxLat, lowerBoxLon, positionCoordinateLat, positionCoordinateLon);
    }

    private boolean calculateBoxLogic(double upperBoxLat, double upperBoxLon, double lowerBoxLat, double lowerBoxLon, double positionCoordinateLat, double positionCoordinateLon) {
        if (upperBoxLat >= positionCoordinateLat && lowerBoxLat <= positionCoordinateLat && upperBoxLon >= positionCoordinateLon && lowerBoxLon <= positionCoordinateLon) {
            return true;
        }
        return false;
    }
}
