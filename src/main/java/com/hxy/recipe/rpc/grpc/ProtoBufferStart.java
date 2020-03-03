package com.hxy.recipe.rpc.grpc;

import com.hxy.rpc.grpc.model.HelloRequest;

public class ProtoBufferStart {

    public static void main(String[] args) {
        HelloRequest helloRequest = HelloRequest.newBuilder()
            .setReqMsg("ping")
            .build();
    }

}
