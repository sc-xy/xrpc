package top.sc_xy.xrpc.registry.impl;

import top.sc_xy.xrpc.common.ServiceMeta;
import top.sc_xy.xrpc.registry.RegistryService;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
public class EurekaRegistryService implements RegistryService {
    public EurekaRegistryService(String registryAddr) {
        // TODO
    }

    @Override
    public void register(ServiceMeta serviceMeta) {

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) {
        return null;
    }

    @Override
    public void destroy() {

    }
}
