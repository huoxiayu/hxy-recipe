package com.hxy.recipe.rpc.thrift;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

@Slf4j
public class ThriftServer implements ThriftService.Iface {

    public static void main(String[] args) throws TTransportException {
        ThriftService.Iface iFace = new ThriftServer();
        TProcessor processor = new ThriftService.Processor(iFace);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(Utils.PORT);
        TNonblockingServer server = new TNonblockingServer(processor, socket, new TFramedTransport.Factory(), new TBinaryProtocol.Factory());
        server.serve();
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
