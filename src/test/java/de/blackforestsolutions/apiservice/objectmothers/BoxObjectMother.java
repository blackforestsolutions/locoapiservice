package de.blackforestsolutions.apiservice.objectmothers;

import org.springframework.data.geo.Point;

public class BoxObjectMother {
    public static Point topLeftPointBonn() {
        Point first = new Point(50.44, 7.6);
        return new Point(first);
    }

    public static Point bottomRightPointStuttgart() {
        Point second = new Point(48.47, 9.194058);
        return new Point(second);
    }

    public static final Point TEST_TOP_LEFT_POINT_1 = new Point(topLeftPointBonn());
    public static final Point TEST_BOTTOM_RIGHT_POINT_1 = new Point(bottomRightPointStuttgart());
}

