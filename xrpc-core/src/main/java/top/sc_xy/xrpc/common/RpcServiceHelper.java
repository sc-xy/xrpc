package top.sc_xy.xrpc.common;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
public class RpcServiceHelper {
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
