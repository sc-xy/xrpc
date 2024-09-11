package top.sc_xy.xrpc.server;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.sc_xy.xrpc.common.RpcProperties;
import top.sc_xy.xrpc.registry.RegistryFactory;
import top.sc_xy.xrpc.registry.RegistryService;
import top.sc_xy.xrpc.registry.RegistryType;

import javax.annotation.Resource;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderAutoConfiguration {
    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RpcProvider init() throws Exception {
        RegistryType type = RegistryType.valueOf(rpcProperties.getRegistryType());
        RegistryService serviceRegistry = RegistryFactory.getInstance(rpcProperties.getRegistryAddr(), type);
        return new RpcProvider(rpcProperties.getServicePort(), serviceRegistry);
    }
}
