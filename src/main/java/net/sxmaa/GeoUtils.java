package net.sxmaa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.*;

public class GeoUtils {

    public static float calculateRotation( Point current, Point previous ) {
        // a = previous
        // b = current
        // since this formula is for radians, we have to convert all values within cos / sin operations to
        // radians first, and the opposite at the end to get a useful bearing
        // X = cos θb * sin ∆L
        // Y = cos θa * sin θb – sin θa * cos θb * cos ∆L
        // beta = atan2(Y, X)
        double deltaL = deg2rad( previous.getLongitude() - current.getLongitude() );

        double X = cos(deg2rad(current.getLatitude())) * sin(deltaL);
        double Y = cos(deg2rad(previous.getLatitude())) *
                    sin(deg2rad(current.getLatitude())) -
                    sin(deg2rad(previous.getLatitude())) *
                    cos(deg2rad(current.getLatitude())) *
                    cos(deltaL);

        double beta = Math.atan2(Y, X);

        // convert to degrees, shorten to float since we don't need crazy precision
        return (float) (beta / Math.PI * 180);
    }

    public static double calculateSpeed(Point current, Point previous, LocalDateTime previousTime, LocalDateTime currentTime ) {
        long deltaT = ChronoUnit.MILLIS.between(currentTime, previousTime);
        double distance = calculateDistance(current, previous);
        double speed = distance / deltaT; // In Kilometers per Millisecond (km/ms)
        speed *= 1000; // In Kilometers per Second (km/s)
        return speed * 3600; // In Kilometers per Hour (km/h)
    }

    public static double calculateDistance(Point start, Point destination) {
        double lat1 = start.getLatitude();
        double lon1 = start.getLongitude();
        double lat2 = destination.getLatitude();
        double lon2 = destination.getLongitude();

        int r = 6371;
        double p = PI / 180;
        double a = 0.5 - cos((lat2-lat1)*p)/2 + cos(lat1*p) * cos(lat2*p) * (1-cos((lon2-lon1)*p))/2;

        return 2 * r * asin(sqrt(a));
    }

    public static double calculateRateOfClimb( Point current, Point previous, LocalDateTime previousTime, LocalDateTime currentTime ) {
        double deltaT = ChronoUnit.MILLIS.between(currentTime, previousTime); // In seconds
        double deltaH = current.getHeight() - previous.getHeight();
        return (deltaH / deltaT) * 3600; // In Feet per Minute (ft/s)
    }
    private static double deg2rad(double degree) {
        return degree * (Math.PI/180);
    }
}
