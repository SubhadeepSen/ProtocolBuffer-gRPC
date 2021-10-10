package com.calculator.client;

import com.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    private static final String HOST = "localhost";
    private static final int PORT = 50031;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .usePlaintext()
                .build();

        System.out.println("\nSimple/Unary RPC:");
        unaryRpcExample(channel);

        System.out.println("\nServer Streaming RPC:");
        serverStreamingRpcExample(channel);

        System.out.println("\nClient Streaming RPC:");
        clientStreamingRpcExample(channel);

        System.out.println("\nBidirectional Streaming RPC:");
        bidirectionalStreamingRpcExample(channel);

        channel.shutdown();
    }

    private static void unaryRpcExample(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub syncCalculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
        CalculatorRequest calculatorRequest = CalculatorRequest.newBuilder()
                .setFirstNumber(20)
                .setSecondNumber(10)
                .build();

        CalculatorResponse calculatorResponse = syncCalculatorClient.add(calculatorRequest);
        long result = calculatorResponse.getResult();
        System.out.println("Adding 20 + 10 = " + result);

        calculatorResponse = syncCalculatorClient.sub(calculatorRequest);
        result = calculatorResponse.getResult();
        System.out.println("Subtracting 20 - 10 = " + result);
    }

    private static void serverStreamingRpcExample(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub syncCalculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
        IntegerRequest integerRequest = IntegerRequest.newBuilder()
                .setEndNumber(10)
                .build();

        Iterator<IntegerStreamResponse> streamResponseIterator = syncCalculatorClient.integerStream(integerRequest);
        streamResponseIterator.forEachRemaining(response -> {
            System.out.println("Integer Stream: " + response.getResult());
        });
    }

    private static void clientStreamingRpcExample(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncCalculatorClient = CalculatorServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<AverageResponse> streamObserverAverageResponse= new StreamObserver<AverageResponse>() {
            @Override
            public void onNext(AverageResponse response) {
                System.out.println("Average: " + response.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        StreamObserver<AverageStreamRequest> streamObserverAverageRequest = asyncCalculatorClient.average(streamObserverAverageResponse);

        Arrays.asList(1,2,3).stream().forEach(number -> {
            System.out.println("Streaming " + number);
            AverageStreamRequest averageStreamRequest = AverageStreamRequest.newBuilder()
                    .setNumber(number)
                    .build();
            streamObserverAverageRequest.onNext(averageStreamRequest);
        });

        streamObserverAverageRequest.onCompleted();

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void bidirectionalStreamingRpcExample(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncCalculatorClient = CalculatorServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<NumberStreamResponse> streamObserverNumberStreamResponse = new StreamObserver<NumberStreamResponse>() {
            @Override
            public void onNext(NumberStreamResponse response) {
                System.out.println("Maximum: " + response.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        StreamObserver<NumberStreamRequest> streamObserverNumberStreamRequest = asyncCalculatorClient.runningMaximum(streamObserverNumberStreamResponse);

        Arrays.asList(3,2,8,4,9,1,6,22,11,56,34,26,75,34,15,48).stream().forEach(number -> {
            System.out.println("Streaming " + number);
            NumberStreamRequest numberStreamRequest = NumberStreamRequest.newBuilder()
                    .setNumber(number)
                    .build();
            streamObserverNumberStreamRequest.onNext(numberStreamRequest);
        });

        streamObserverNumberStreamRequest.onCompleted();

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
