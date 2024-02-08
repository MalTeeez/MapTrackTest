package net.sxmaa;

import io.prometheus.client.exporter.HTTPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;

public class Main {
    private static HTTPServer http_server;

    private static MetricCollector collector;
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static PointRecord pr = new PointRecord();

    public static void main(String[] args) throws IOException {
        initHttpServer();
        initCollectors();

        ExampleData.readFromFile();
        System.out.println("Finished processing and sending all flights, exiting.");
        http_server.close();
    }

    private static void initHttpServer() throws IOException {
        int port = 19501;
        String address = "0.0.0.0";
        try {
            http_server = new HTTPServer(address, port, true);
            System.out.println("HTTPServer listening on http://" + "localhost:" + http_server.getPort() + "/metrics");
        } catch (BindException e) {
            System.out.println("Failed to start prometheus MTT, port " + port + " already in use.");
        }
    }

    private static void initCollectors() {
        collector = new MetricCollector();
        collector.register();
    }

}