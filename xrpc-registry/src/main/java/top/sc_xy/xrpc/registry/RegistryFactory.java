package top.sc_xy.xrpc.registry;

import top.sc_xy.xrpc.registry.impl.EurekaRegistryService;
import top.sc_xy.xrpc.registry.impl.ZookeeperRegistryService;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
public class RegistryFactory {
    private static volatile RegistryService registryService;

    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {

        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryService(registryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryService(registryAddr);
                            break;
                    }
                }
            }
        }
        return registryService;
    }
}
