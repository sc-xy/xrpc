package top.sc_xy.xrpc.server;

public interface ServerProviderRegister {
    /**
     * 添加服务提供者
     *
     * @param serverClass    服务类Class
     * @param serverProvider 服务提供实例
     * @param <T>            服务类别
     */
    <T> void addServerProvider(Class<? extends T> serverClass, T serverProvider);
}
