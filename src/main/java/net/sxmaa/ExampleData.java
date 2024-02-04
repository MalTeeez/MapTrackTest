package net.sxmaa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ExampleData {


    public static Point getRandomWLD() {
        Random r = new Random();
        double lon = r.nextDouble(-90, 91);
        double lat = r.nextDouble(-180, 181);
        double rot = r.nextDouble(0, 361);
        int hei = r.nextInt(0, 35000);
        return new Point(lat, lon, rot, hei);
    }

    public static Point getRandomDEU() {
        Random r = new Random();
        double lat = r.nextDouble(46, 55);
        double lon = r.nextDouble(5, 15);
        double rot = r.nextDouble(0, 361);
        int hei = r.nextInt(0, 35000);
        return new Point(lat, lon, rot, hei);
    }

    public static void readFromFile() {
        try(BufferedReader br = new BufferedReader(new FileReader("output.txt"))) {
            System.out.println("Starting file read");
            String line = br.readLine();
            LocalDateTime startTime = getTime( line.split(",") );
            while (line != null) {
                String[] l = line.split(",");

                LocalDateTime time = getTime(l);
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
        int altitude = Integer.parseInt(l[3]) * 100;
        //Got bad readings from this guy
        altitude = altitude == 204700 ? 0 : altitude;

        String lat_s = l[4].replaceAll("[^0-9]+", "");
        double latitude = Double.parseDouble(lat_s.substring(0, 2));
        latitude += Double.parseDouble(lat_s.substring(2, 4)) / 60;
        latitude += Double.parseDouble(lat_s.substring(4, 6)) / 3600;

        String lon_s = l[5].replaceAll("[^0-9]+", "");
        double longitude = Double.parseDouble(lon_s.substring(0, 3));
        longitude += Double.parseDouble(lon_s.substring(3, 5)) / 60;
        longitude += Double.parseDouble(lon_s.substring(5, 7)) / 3600;

        return new Point(id, latitude, longitude, altitude);
    }

    private static LocalDateTime getTime(String[] l) {
        String[] times = l[0].split(":", 3);
        LocalDate date = LocalDate.now();
        return date.atTime(
                Integer.parseInt(times[0]),
                Integer.parseInt(times[1]),
                Integer.parseInt(times[2].substring(0,2)),
                Integer.parseInt(times[2].substring(3,4)) * 100000000
        );
    }
}
