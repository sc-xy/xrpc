package top.sc_xy.xrpc.common;

import lombok.Data;

/**
 * @author sc-xy
 * @time 2024/9/9
 */
@Data
public class ServiceMeta {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;
}
