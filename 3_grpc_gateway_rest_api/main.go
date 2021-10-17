package main

import (
	"context"
	"log"
	"net"
	"net/http"

	pb "com.grpc.example/src/calculatorpb"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"google.golang.org/grpc"
)

const (
	grpc_port = ":50031"
	http_port = "8081"
)

type server struct {
	pb.UnimplementedCalculatorServiceServer
}

func (s *server) Add(ctx context.Context, request *pb.CalculatorRequest) (*pb.CalculatorResponse, error) {
	firstnumber := request.GetFirstNumber()
	secondNumber := request.GetSecondNumber()
	log.Printf("Adding numbers: %v and %v", firstnumber, secondNumber)
	return &pb.CalculatorResponse{Result: (firstnumber + secondNumber)}, nil
}

func (s *server) Sub(ctx context.Context, request *pb.CalculatorRequest) (*pb.CalculatorResponse, error) {
	firstnumber := request.GetFirstNumber()
	secondNumber := request.GetSecondNumber()
	log.Printf("Subtracting numbers: %v and %v", firstnumber, secondNumber)
	return &pb.CalculatorResponse{Result: (firstnumber - secondNumber)}, nil
}

func main() {

	go func() {
		log.Println("starting http server...")
		mux := runtime.NewServeMux()
		pb.RegisterCalculatorServiceHandlerServer(context.Background(), mux, &server{})
		log.Printf("http server listening at localhost:%v", http_port)
		log.Fatal(http.ListenAndServe("localhost:"+http_port, mux))
	}()

	listener, err := net.Listen("tcp", grpc_port)
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	grpcServer := grpc.NewServer()
	pb.RegisterCalculatorServiceServer(grpcServer, &server{})
	log.Println("starting grpc server...")
	log.Printf("grpc server listening at %v", listener.Addr())
	err = grpcServer.Serve(listener)
	if err != nil {
		log.Fatalf("Failed to server: %v", err)
	}
}
