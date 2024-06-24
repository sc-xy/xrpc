package top.sc_xy.xrpc.client.stubs;

import top.sc_xy.xrpc.client.RequestIdSupport;
import top.sc_xy.xrpc.client.ServerStub;
import top.sc_xy.xrpc.client.ServerTypes;
import top.sc_xy.xrpc.serialize.SerializerSupport;
import top.sc_xy.xrpc.transport.Transport;
import top.sc_xy.xrpc.transport.command.Code;
import top.sc_xy.xrpc.transport.command.Command;
import top.sc_xy.xrpc.transport.command.Header;
import top.sc_xy.xrpc.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

public class AbstractStub implements ServerStub {
    protected Transport transport;

    protected byte[] invokeRemote(RpcRequest request) {
        Header header = new Header(RequestIdSupport.nextId(), 1, ServerTypes.TYPE_RPC_REQUEST);
        byte[] payload = SerializerSupport.serialize(request);
        Command requestCommand = new Command(header, payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
