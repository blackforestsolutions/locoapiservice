package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.data.geo.Box;
import org.springframework.stereotype.Service;

@Service
public class BoxServiceImpl implements BoxService {

    @Override
    public boolean isCoordinateInBox(Coordinates coordinates, Box providerBox) {
        double leftTopLat = providerBox.getFirst().getY();
        double leftTopLon = providerBox.getFirst().getX();
        double rightBottomLat = providerBox.getSecond().getY();
        double rightBottomLon = providerBox.getSecond().getX();

        return coordinates.getLatitude() <= leftTopLat && coordinates.getLatitude() >= rightBottomLat
                &&
                coordinates.getLongitude() >= leftTopLon && coordinates.getLongitude() <= rightBottomLon;
    }
}
