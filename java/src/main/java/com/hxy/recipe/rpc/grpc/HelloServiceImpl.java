package com.hxy.recipe.rpc.grpc;

import com.hxy.rpc.grpc.model.HelloReply;
import com.hxy.rpc.grpc.model.HelloRequest;
import com.hxy.rpc.grpc.model.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hi(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        log.info("server receive {}", request.getReqMsg());
        HelloReply reply = HelloReply.newBuilder()
            .setRespMsg("hi I am server")
            .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}