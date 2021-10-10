package com.calculator.server;

import com.calculator.service.CalculatorService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class CalculatorServer {

    private static final int PORT = 50031;

    public static void main(String[] args) {
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new CalculatorService())
                //.addService(ProtoReflectionService.newInstance())
                .build();

        try {
            server.start();
            System.out.println("server started, listening on port " + PORT);
            server.awaitTermination();
        } catch (Exception e) {
            System.out.println("unable to start server: " + e.getMessage());
            server.shutdownNow();
        }
    }
}
