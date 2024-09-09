package top.sc_xy.xrpc.registry;

import top.sc_xy.xrpc.common.ServiceMeta;

import java.io.IOException;

/**
 * @author sc-xy
 * @time 2024/9/9
 */
public interface RegistryService {
    void register(ServiceMeta serviceMeta) throws Exception;

    void unRegister(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    void destroy() throws IOException;
}
