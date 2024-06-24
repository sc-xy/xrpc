package top.sc_xy.xrpc.transport;

import top.sc_xy.xrpc.transport.RequestHandlerRegister;

public interface TransportServer extends Cloneable{
    void start(RequestHandlerRegister requestHandlerRegister, int port) throws Exception;

    void stop();
}
