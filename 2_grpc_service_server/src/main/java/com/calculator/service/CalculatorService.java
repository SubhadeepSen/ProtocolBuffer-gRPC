package com.calculator.service;

import com.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorService extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void add(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
        long firstNumber = request.getFirstNumber();
        long secondNumber = request.getSecondNumber();
        long sumResult = firstNumber + secondNumber;

        CalculatorResponse calculatorResponse = CalculatorResponse.newBuilder()
                .setResult(sumResult)
                .build();

        responseObserver.onNext(calculatorResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void sub(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
        long firstNumber = request.getFirstNumber();
        long secondNumber = request.getSecondNumber();
        long subtractedResult = firstNumber - secondNumber;

        CalculatorResponse calculatorResponse = CalculatorResponse.newBuilder()
                .setResult(subtractedResult)
                .build();

        responseObserver.onNext(calculatorResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void integerStream(IntegerRequest request, StreamObserver<IntegerStreamResponse> responseObserver) {
        long endNumber = request.getEndNumber();
        long number = 1;

        while (number <= endNumber){
            IntegerStreamResponse integerStreamResponse = IntegerStreamResponse.newBuilder()
                    .setResult(number)
                    .build();
            responseObserver.onNext(integerStreamResponse);
            number++;
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<AverageStreamRequest> average(StreamObserver<AverageResponse> responseObserver) {
        return new StreamObserver<AverageStreamRequest>() {
            long sum = 0;
            int count = 0;

            @Override
            public void onNext(AverageStreamRequest request) {
                long number = request.getNumber();
                sum += number;
                count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                long average = sum / count;
                AverageResponse averageResponse = AverageResponse.newBuilder()
                        .setResult(average)
                        .build();
                responseObserver.onNext(averageResponse);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<NumberStreamRequest> runningMaximum(StreamObserver<NumberStreamResponse> responseObserver) {
        return new StreamObserver<NumberStreamRequest>() {
            long max = 0;

            @Override
            public void onNext(NumberStreamRequest request) {
                long number = request.getNumber();
                if (number > max) {
                    max = number;
                }
                NumberStreamResponse numberStreamResponse = NumberStreamResponse.newBuilder()
                        .setResult(max)
                        .build();
                responseObserver.onNext(numberStreamResponse);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
