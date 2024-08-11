package net.sxmaa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ExampleData {


    public static Point getRandomWLD() {
        double lon = generateDouble(-90, 91);
        double lat = generateDouble(-180, 181);
        double rot = generateDouble(0, 361);
        int hei = generateInt(0, 35000);
        return new Point(lat, lon, rot, hei);
    }

    public static Point getRandomDEU() {
        double lat = generateDouble(46, 55);
        double lon = generateDouble(5, 15);
        double rot = generateDouble(0, 361);
        int hei = generateInt(0, 35000);
        return new Point(lat, lon, rot, hei);
    }

    private static double generateDouble(int min, int max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    private static int generateInt(int min, int max) {
        Random r = new Random();
        return min + (max - min) * r.nextInt();
    }

    public static void readFromFile() {
        try(BufferedReader br = new BufferedReader(new FileReader("output.txt"))) {
            System.out.println("Starting file read");
            String line = br.readLine();
            LocalTime startTime = getTime( line.split(",") );
            while (line != null) {
                String[] l = line.split(",");

                LocalTime time = getTime(l);
                final Point p = getPoint(l);
                p.setTime(time);
                if ( ChronoUnit.MINUTES.between(startTime, time) > 5)
                    System.out.println("Current time is:" + time + ", sleeping for " + ChronoUnit.SECONDS.between(startTime, time) + " seconds.");
                try {
                    sleep(Math.abs(ChronoUnit.MILLIS.between(time, startTime)));
                } catch (InterruptedException e) {
                    System.out.println("Sleep interrupted");
                }
                startTime = time;

                Main.pr.addToRecord(p, p.getId(), time);
                line = br.readLine();
            }
        } catch ( IOException e ) {
            System.out.println(e.getMessage());
        }
    }

    private static Point getPoint(String[] l) {
        String id = l[1];
        String squawk = l[2];
        String callsign = l[3];
        int altitude = Integer.parseInt(l[4]) * 100;
        // Exclude bad readings, since we don't track planes in space (satellites?)
        if ( altitude > 60000 ) altitude = 0;
        // Only set id to callsign if it actually exists for this plane, otherwise fallback to numeric id
        id = !Objects.equals(callsign, "") ? callsign : id;

        // Using 5 decimal places, see https://gis.stackexchange.com/a/8674
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        String lat_s = l[5].replaceAll("[^0-9]+", "");
        double latitude = Double.parseDouble(lat_s.substring(0, 2));
        latitude += Double.parseDouble(lat_s.substring(2, 4)) / 60;
        latitude += Double.parseDouble(lat_s.substring(4, 6)) / 3600;
        latitude = roundDecimals(latitude);


        String lon_s = l[6].replaceAll("[^0-9]+", "");
        double longitude = Double.parseDouble(lon_s.substring(0, 3));
        longitude += Double.parseDouble(lon_s.substring(3, 5)) / 60;
        longitude += Double.parseDouble(lon_s.substring(5, 7)) / 3600;
        longitude = roundDecimals(longitude);

        return new Point(id, latitude, longitude, altitude);
    }

    private static LocalTime getTime(String[] l) {
        String[] times = l[0].split(":", 3);
        return LocalTime.of(
                Integer.parseInt(times[0]),
                Integer.parseInt(times[1]),
                Integer.parseInt(times[2].substring(0,2)),
                Integer.parseInt(times[2].substring(3,4)) * 100000000
        );
    }

    private static double roundDecimals(double value) {
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return Double.parseDouble(df.format(value));
    }

}
