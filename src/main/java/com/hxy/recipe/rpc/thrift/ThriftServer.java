package com.hxy.recipe.rpc.thrift;

import com.hxy.recipe.thrift.Request;
import com.hxy.recipe.thrift.Response;
import com.hxy.recipe.thrift.ThriftService;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ThriftServer implements ThriftService.Iface {

    public static void main(String[] args) throws Exception {
        byteBuddy();

        ThriftService.Iface iFace = new ThriftServer();
        TProcessor processor = new ThriftService.Processor(iFace);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(Utils.PORT);
        TNonblockingServer server = new TNonblockingServer(processor, socket, new TFramedTransport.Factory(), new TBinaryProtocol.Factory());

        server.serve();

        //        Response response = new Response();
        //        response.setId(666L);
        //        response.setMsg("byte[]");
        //        ThriftService.send_result send_result = new ThriftService.send_result();
        //        send_result.success = response;
        //        TSerializer tSerializer = new TSerializer(new TBinaryProtocol.Factory());
        //        byte[] bytes = tSerializer.serialize(send_result);
        //
        //        TProtocol o = new TBinaryProtocol(null);
        //        o.getTransport().write(bytes);
        //        o.getTransport().flush();
    }

    private static void byteBuddy() throws ClassNotFoundException {
        ByteBuddyAgent.install();

        String clazz = "com.hxy.recipe.rpc.thrift.ThriftService$Processor$send";
        Class<?> aClass = Class.forName(clazz);
        log.info("{}", aClass.getName());

        try {
            new ByteBuddy()
                .redefine(aClass)
                .method(ElementMatchers.named("process"))
                .intercept(MethodDelegation.to(DelegationServer.class))
                .make()
                .load(aClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .saveIn(new File("/Users/hxy/code/tmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("redefine finish");
    }

    @Override
    public Response send(Request request) {
        log.info("receive req -> {}", request);
        Response response = new Response();
        response.setId(request.getId());
        response.setMsg("server msg");
        response.setRequest(request);
        log.info("return resp -> {}", response);
        return response;
    }

}
