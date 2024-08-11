package net.sxmaa;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointRecord {

    private Map<String, Point> points = new HashMap<>();

    private Map<String, Point> prevPoints = new HashMap<>();

    public void clearCurrent( LocalTime time ) {
        List<String> toDelete = new ArrayList<>();
        for ( Map.Entry<String, Point> entry : points.entrySet() ) {
            if ( ChronoUnit.MINUTES.between(entry.getValue().getTime(), time) > 1 ) {
                toDelete.add(entry.getKey());
                //System.out.println("Point " + entry.getValue().getId() + " was removed because of age.");
            }
        }
        for ( String key : toDelete ) {
            points.remove(key);
        }
        toDelete.clear();

        for ( Map.Entry<String, Point> entry : prevPoints.entrySet() ) {
            if ( ChronoUnit.MINUTES.between(entry.getValue().getTime(), time) > 2 ) {
                toDelete.add(entry.getKey());
                //System.out.println("Previous Point " + entry.getValue().getId() + " was removed because of age.");
            }
        }
        for ( String key : toDelete ) {
            prevPoints.remove(key);
        }
        toDelete.clear();
    }

    public void addToRecord( Point p, String id, LocalTime time ) {
        if ( prevPoints.containsKey(id) ) {
            //System.out.println("Point " + p.getId() + " already existed, calculating deltas.");
            p.setRotation(GeoUtils.calculateRotation(p, prevPoints.get(id)));
            p.setSpeed(GeoUtils.calculateSpeed(
                    p,
                    prevPoints.get(id),
                    time,
                    prevPoints.get(id).getTime()
                )
            );
            p.setRateofclimb(GeoUtils.calculateRateOfClimb(
                    p,
                    prevPoints.get(id),
                    time,
                    prevPoints.get(id).getTime()
                )
            );
        }
        if ( points.containsKey(id) ) {
            //System.out.println("Point " + p.getId() + " already existed moving it back to previous points.");
            prevPoints.put(id, points.get(id));
        } else {
            //System.out.println("New Point " + p.getId() + " added.");
        }

        points.put(id, p);
        clearCurrent(time);
    }

    public List<Point> getPoints() {
        return new ArrayList<>(points.values());
    }

}
