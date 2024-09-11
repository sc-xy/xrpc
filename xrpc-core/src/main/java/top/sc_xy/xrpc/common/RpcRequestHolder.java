package top.sc_xy.xrpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
public class RpcRequestHolder {
    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
