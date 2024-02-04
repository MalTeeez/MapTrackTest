package net.sxmaa;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricCollector extends Collector implements Collector.Describable{


    @Override
    public List<MetricFamilySamples> collect() {
        MetricFamilySamples point_list = this.collectPointList();
        ArrayList<MetricFamilySamples> metrics = new ArrayList<>(
                1 /* point_list */
        );

        metrics.add(point_list);

        return metrics;
    }

    private MetricFamilySamples collectPointList() {
        GaugeMetricFamily metric = newPointListMetric();
        //for ( int i = 0; i < r.nextInt(500, 1001); i++ ) {
        //    Point p = ExampleData.getRandomDEU();
        for ( Point p : Main.pr.getPoints() ) {
            //System.out.println(p.toString());
            metric.addMetric(Arrays.asList(p.getId(), "lat"), p.getLatitude());
            metric.addMetric(Arrays.asList(p.getId(), "lon"), p.getLongitude());
            metric.addMetric(Arrays.asList(p.getId(), "rot"), p.getRotation());
            metric.addMetric(Arrays.asList(p.getId(), "hei"), p.getHeight());
        }

        return metric;
    }


    @Override
    public List<MetricFamilySamples> describe() {
        List<MetricFamilySamples> descs = new ArrayList<>();
        descs.add(newPointListMetric());

        return descs;
    }

    private static GaugeMetricFamily newPointListMetric() {
        return new GaugeMetricFamily(
                "point_list",
                "List of points with coordinates, rotation and id",
                Arrays.asList("id", "type")
        );
    }
}
