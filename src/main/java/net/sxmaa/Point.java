package net.sxmaa;

import java.time.LocalTime;
import java.util.Random;

public class Point {

    private String id;
    private double latitude;
    private double longitude;
    private double rotation;
    private int height;
    private double speed;
    private double rateofclimb;
    private LocalTime time;

    public Point(double latitude, double longitude, double rotation, int height) {
        Random rand = new Random();
        this.id = String.valueOf(rand.nextInt(10)); /* between 100.000 and 999.999 */
        this.latitude = latitude;
        this.longitude = longitude;
        this.rotation = rotation;
        this.height = height;
    }

    public Point(double latitude, double longitude) {
        Random rand = new Random();
        this.id = String.valueOf(rand.nextInt(10)); /* between 100.000 and 999.999 */
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(String id, double latitude, double longitude, int height) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRotation() {
        return rotation;
    }

    public int getHeight() {
        return height;
    }

    public double getSpeed() {
        return speed;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getRateofclimb() {
        return rateofclimb;
    }

    public void setRateofclimb(double rateofclimb) {
        this.rateofclimb = rateofclimb;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public static Point from(String s, double rot, int height) {
        String[] l = s.split(",");
        return new Point(Double.parseDouble(l[0]), Double.parseDouble(l[1]), rot, height);
    }

    @Override
    public String toString() {
        return "Point Information: \n" +
                "\n\tId:" + id +
                "\n\tLatitude:" + latitude +
                "\n\tLongitude:" + longitude +
                "\n\tRotation:" + rotation +
                "\n\tHeight:" + height +
                "\n\tSpeed:" + speed +
                "\n\tRate of Climb:" + rateofclimb;
    }
}
