package com.hxy.recipe.rpc.grpc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hxy.rpc.grpc.model.CompositeHello;
import com.hxy.rpc.grpc.model.Gender;
import com.hxy.rpc.grpc.model.HelloRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ProtoBufferStart {

    public static void main(String[] args) {
        protoBuffer();
    }

    private static void protoBuffer() {
        CompositeHello serialize = CompositeHello.newBuilder()
            .setMsg("msg")
            .addMsgList("msgItem")
            .setId(1)
            .setHelloRequest(HelloRequest.newBuilder().setReqMsg("hello"))
            .setSkip(false)
            .addInfoList(CompositeHello.Info.newBuilder().setName("info").setTimestamp(System.currentTimeMillis()))
            .setGender(Gender.MAN)
            .putAllStrMap(Map.of("ext", "ext_info"))
            .build();

        log.info("serialize: {}", serialize);

        byte[] bytes = serialize.toByteArray();
        log.info("serialize bytes: {}", bytes);

        try {
            CompositeHello deserialize = CompositeHello.parseFrom(bytes);
            log.info("deserialize: {}", deserialize);

            log.info("consistent: {}", serialize == deserialize);

            log.info("equals: {}", serialize.equals(deserialize));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
