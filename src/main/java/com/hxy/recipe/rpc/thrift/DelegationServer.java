package com.hxy.recipe.rpc.thrift;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;

@Slf4j
public class DelegationServer {

    public static void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
        log.info("call delegate");
        ThriftService.send_args args = new ThriftService.send_args();
        try {
            args.read(iprot);
        } catch (TProtocolException e) {
            iprot.readMessageEnd();
            TApplicationException x = new TApplicationException(TApplicationException.PROTOCOL_ERROR, e.getMessage());
            oprot.writeMessageBegin(new TMessage("send", TMessageType.EXCEPTION, seqid));
            x.write(oprot);
            oprot.writeMessageEnd();
            oprot.getTransport().flush();
            return;
        }
        iprot.readMessageEnd();
        ThriftService.send_result result = new ThriftService.send_result();
        result.success = (new ThriftServer()).send(args.request);
        oprot.writeMessageBegin(new TMessage("send", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
    }

}
