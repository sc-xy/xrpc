package top.sc_xy.xrpc;

import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心服务
 */
public interface NameServer {
    /**
     * 注册服务
     *
     * @param serverName 服务名
     * @param uri        服务地址
     * @throws IOException 抛出IO异常
     */
    void registerServer(String serverName, URI uri) throws IOException;

    /**
     * 查询服务
     *
     * @param serverName 服务名
     * @return 服务地址
     * @throws IOException 抛出IO异常
     */
    URI findServer(String serverName) throws IOException;

    /**
     * 所有支持的协议
     */
    Collection<String> supportedSchemes();

    /**
     * 连接注册中心
     *
     * @param nameServerUri 注册中心地址
     */
    void connect(URI nameServerUri);
}
