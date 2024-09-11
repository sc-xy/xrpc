package top.sc_xy.xrpc.registry.loadbalancer;

import java.util.List;

/**
 * @author sc-xy
 * @time 2024/9/10
 */
public interface ServiceLoadBalancer<T> {
    T select(List<T> list, int hashCode);
}
