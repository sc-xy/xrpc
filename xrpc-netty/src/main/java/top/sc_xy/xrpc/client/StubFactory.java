package top.sc_xy.xrpc.client;

import top.sc_xy.xrpc.transport.Transport;

/**
 * 桩工厂
 */
public interface StubFactory {
    /**
     * 创建桩对象实例
     *
     * @param transport   给服务端发送请求的通信对象
     * @param serverClass 桩的类型 Class
     * @param <T>         桩的类型
     * @return 桩实例
     */
    <T> T createStub(Transport transport, Class<T> serverClass);
}
