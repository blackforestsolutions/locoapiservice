package de.blackforestsolutions.apiservice.util;

import de.blackforestsolutions.datamodel.Coordinates;

public class CoordinatesUtil {

    private static final int HUNDREDTHS_MERIDIAN_WEST = 100;
    private static final int THIRTY_SIX_PARALLEL_NORTH = 36;
    // Means 7.438637222222222 degree east
    private static final int SWISS_TOPOGRAPHY_BERN_EAST = 600000;
    // Means 46.9510811111111 degree north
    private static final int SWISS_TOPOGRAPHY_BERN_NORTH = 200000;
    private static final int SWISS_TOPOGRAPHY_MULTIPLIER_CH1903 = 1000000;

    private static final int WGS84_EXP = 6;
    private static final int WGS84_BASE = 10;

    /**
     * This method transforms a ch1903 coordinate format to latitude and longitude
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return transformed date
     */
    public static Coordinates convertCh1903ToCoordinatesWith(int x, int y) {
        return new Coordinates.CoordinatesBuilder(
                convertCh1903CoordinatesToLatitude(y, x),
                convertCh1903CoordinatesToLangitude(y, x)
        ).build();
    }

    /**
     * This method transforms a WGS84 coordinate format to latitude and longitude
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return transformed date
     */
    public static Coordinates convertWGS84ToCoordinatesWith(int x, int y) {
        Coordinates.CoordinatesBuilder coordinates = new Coordinates.CoordinatesBuilder();
        coordinates.setLongitude((double) x / Math.pow(WGS84_BASE, WGS84_EXP));
        coordinates.setLatitude((double) y / Math.pow(WGS84_BASE, WGS84_EXP));
        return coordinates.build();
    }

    private static double convertCh1903CoordinatesToLatitude(double y, double x) {
        double yAux = (y - SWISS_TOPOGRAPHY_BERN_EAST) / SWISS_TOPOGRAPHY_MULTIPLIER_CH1903;
        double xAux = (x - SWISS_TOPOGRAPHY_BERN_NORTH) / SWISS_TOPOGRAPHY_MULTIPLIER_CH1903;
        double lat = calculateSwissLatitude(xAux, yAux);
        return normalize(lat);
    }

    private static double convertCh1903CoordinatesToLangitude(double y, double x) {
        double yAux = (y - SWISS_TOPOGRAPHY_BERN_EAST) / SWISS_TOPOGRAPHY_MULTIPLIER_CH1903;
        double xAux = (x - SWISS_TOPOGRAPHY_BERN_NORTH) / SWISS_TOPOGRAPHY_MULTIPLIER_CH1903;
        double lng = calculateSwissLongitude(xAux, yAux);
        return normalize(lng);
    }

    private static double normalize(double coordinate) {
        return (coordinate * HUNDREDTHS_MERIDIAN_WEST) / THIRTY_SIX_PARALLEL_NORTH;
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static double calculateSwissLatitude(double xAux, double yAux) {
        return 16.9023892 + (3.238272 * xAux)
                - 0.270978 * Math.pow(yAux, 2)
                - 0.002528 * Math.pow(xAux, 2)
                - 0.0447 * Math.pow(yAux, 2) * xAux
                - 0.0140 * Math.pow(xAux, 3);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static double calculateSwissLongitude(double xAux, double yAux) {
        return 2.6779094 + (4.728982 * yAux)
                + (0.791484 * yAux * xAux)
                + (0.1306 * yAux * Math.pow(xAux, 2))
                - 0.0436 * Math.pow(yAux, 3);
    }
}
