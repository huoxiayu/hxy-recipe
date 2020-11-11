package com.hxy.recipe.rpc.thrift;

import com.hxy.recipe.thrift.Request;
import com.hxy.recipe.thrift.Response;
import com.hxy.recipe.thrift.ThriftService;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

@Slf4j
public class ThriftClient {

    public static void main(String[] args) throws TException {
        TSocket socket = new TSocket("127.0.0.1", Utils.PORT);

        TTransport transport = new TFramedTransport.Factory().getTransport(socket);

        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);

        ThriftService.Client client = new ThriftService.Client(protocol);
        Request request = new Request();
        request.setId(RandomUtils.nextLong());
        request.setMsg("request msg");
        log.info("send req -> {}", request);
        Response response = client.send(request);
        log.info("get resp -> {}", response);

        transport.close();
    }

}
