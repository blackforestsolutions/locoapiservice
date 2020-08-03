package de.blackforestsolutions.apiservice.objectmothers;

import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

import java.util.UUID;

public class BoxObjectMother {

    public static Box rmvBox() {
        Point first = new Point(50.117269, 8.729453);
        Point second = new Point(48.768033, 9.194058);
        return new Box(first,second);
    }
    public static Point fist() {
        Point first = new Point(50.117269, 8.729453);
        return new Point(first);
    }
    public static Point second() {
        Point second = new Point(48.768033, 9.194058);
        return new Point(second);
    }
    public static final Point TEST_POINT_1 = new Point(fist());
    public static final Point TEST_POINT_2 = new Point(second());
}
