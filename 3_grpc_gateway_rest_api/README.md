1. Download ProtocolBuffer compiler from https://github.com/protocolbuffers/protobuf/releases

2. Install the following packages
`
go get -u github.com/grpc-ecosystem/grpc-gateway/v2/protoc-gen-grpc-gateway

go get -u google.golang.org/protobuf/cmd/protoc-gen-go

go get -u google.golang.org/grpc/cmd/protoc-gen-go-grpc
`

3. Execute `generate-script.bat` script to generate the stubs

4. Start the server by executing main.go

5. Use [BloomRPC](https://github.com/uw-labs/bloomrpc) to test the RPC apis

6. Use Postman to test the http apis

### HTTP APIs:
* http://localhost:8081/v1/add
```json
{
  "first_number": 120,
  "second_number": 100
}
```
* http://localhost:8081/v1/sub
```json
{
  "first_number": 120,
  "second_number": 100
}
```

