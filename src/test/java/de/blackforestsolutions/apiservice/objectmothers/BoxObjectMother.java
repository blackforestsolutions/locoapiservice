package de.blackforestsolutions.apiservice.objectmothers;

import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

public class BoxObjectMother {

    public static Box getTestBox() {
        Point topLeft = new Point(0d, 100d);
        Point bottomRight = new Point(100d, 0d);
        return new Box(topLeft, bottomRight);
    }

    public static Box getRmvBox() {
        Point topLeft = new Point(6.889390, 52.071805d);
        Point bottomRight = new Point(10.772754, 49.052665);
        return new Box(topLeft, bottomRight);
    }
}

