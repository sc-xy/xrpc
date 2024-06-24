package top.sc_xy.xrpc;

import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

/**
 * 对外提供服务的接口
 */
public interface XrpcPoint extends Closeable {
    /**
     * 客户端获取服务端的引用
     *
     * @param uri         服务端的地址
     * @param serverClass 服务接口类的 Class
     * @param <T>         服务的接口类型
     * @return 服务端的引用
     */
    <T> T getRemoteServer(URI uri, Class<T> serverClass);

    /**
     * 服务端注册服务的实现
     *
     * @param server      服务实现示例
     * @param serverClass 服务接口类的 Class
     * @param <T>         服务的接口类型
     * @return 服务地址
     */
    <T> URI registerServerProvider(T server, Class<T> serverClass);

    /**
     * 获取注册中心实例
     *
     * @param nameServerUri 注册中心URI
     * @return 注册中心引用
     */
    default NameServer getNameServer(URI nameServerUri) {
        Collection<NameServer> nameServers = ServerLoadSupport.loadAll(NameServer.class);
        for (NameServer nameServer : nameServers) {
            if (nameServer.supportedSchemes().contains(nameServerUri.getScheme())) {
                nameServer.connect(nameServerUri);
                return nameServer;
            }
        }
        return null;
    }

    /**
     * 服务端启动 RPC 框架，监听接口，提供远程服务
     *
     * @return 服务实例，程序停止时可以安全关闭服务
     */
    Closeable start() throws Exception;
}
