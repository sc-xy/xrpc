package top.sc_xy.xrpc.client;

import top.sc_xy.xrpc.transport.Transport;

/**
 * 远程桩接口
 */
public interface ServerStub {
    /**
     * 设置远程服务调用
     *
     * @param transport 远程服务调用
     */
    void setTransport(Transport transport);
}
