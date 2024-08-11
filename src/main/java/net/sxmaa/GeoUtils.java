package net.sxmaa;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalTime;
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

        double curr_lat_radian = deg2rad(current.getLatitude());
        double prev_lat_radian = deg2rad(previous.getLatitude());
        double X = cos(curr_lat_radian) * sin(deltaL);
        double Y = cos(prev_lat_radian) *
                    sin(curr_lat_radian) -
                    sin(prev_lat_radian) *
                    cos(curr_lat_radian) *
                    cos(deltaL);

        double beta = Math.atan2(Y, X);

        // convert to degrees
        return roundDecimals((beta / Math.PI * 180), 2);
    }

    public static float calculateSpeed(Point current, Point previous, LocalTime previousTime, LocalTime currentTime ) {
        long deltaT = ChronoUnit.MILLIS.between(currentTime, previousTime);
        double distance = calculateDistance(current, previous);
        double speed = distance / deltaT; // In Kilometers per Millisecond (km/ms)
        speed *= 1000; // In Kilometers per Second (km/s)
        if ( speed * 3600 > 5000 ) speed = 0; // Exclude ridiculous speeds, we don't have SR-71's anymore ):
        return roundDecimals(speed * 3600, 3); // In Kilometers per Hour (km/h)
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

    public static float calculateRateOfClimb( Point current, Point previous, LocalTime previousTime, LocalTime currentTime ) {
        double deltaT = ChronoUnit.MILLIS.between(currentTime, previousTime); // In seconds
        double deltaH = current.getHeight() - previous.getHeight();
        return roundDecimals((deltaH / deltaT) * 3600, 3); // In Feet per Minute (ft/s)
    }

    private static double deg2rad(double degree) {
        return degree * (Math.PI/180);
    }

    private static float roundDecimals(double value, int decimals) {
        DecimalFormat df = new DecimalFormat("#." + new String(new char[decimals]).replace("\0", "#"));
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return Float.parseFloat(df.format(value));
    }
}
