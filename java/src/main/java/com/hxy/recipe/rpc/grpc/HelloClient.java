package com.hxy.recipe.rpc.grpc;

import com.hxy.rpc.grpc.model.HelloReply;
import com.hxy.rpc.grpc.model.HelloRequest;
import com.hxy.rpc.grpc.model.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class HelloClient {

    private final ManagedChannel channel;
    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public HelloClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build());
    }

    private HelloClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendMsg(String reqMsg) {
        HelloRequest request = HelloRequest.newBuilder().setReqMsg(reqMsg).build();
        try {
            HelloReply response = blockingStub.hi(request);
            log.info("client receive {}", response);
        } catch (StatusRuntimeException e) {
            log.error("sendMsg.error: {}", e);
        }
    }

    public static void main(String[] args) throws Exception {
        HelloClient client = new HelloClient("127.0.0.1", 50051);

        try {
            int times = 1000;
            for (int i = 0; i < times; i++) {
                long start = System.currentTimeMillis();

                StringBuilder sb = new StringBuilder();
                int len = 1_0000;
                for (int x = 0; x < len; x++) {
                    sb.append(x);
                }

                String input = sb.toString();
                log.info("copy cost {} millis", System.currentTimeMillis() - start);

                client.sendMsg(input);
                log.info("rpc cost {} millis", System.currentTimeMillis() - start);
            }
        } finally {
            client.shutdown();
        }
    }
}