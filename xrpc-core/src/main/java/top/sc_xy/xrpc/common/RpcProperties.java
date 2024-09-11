package top.sc_xy.xrpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
@Data
@ConfigurationProperties(prefix = "xrpc")
public class RpcProperties {

    private int servicePort;

    private String registryAddr;

    private String registryType;

}

