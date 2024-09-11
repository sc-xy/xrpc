package top.sc_xy.xrpc.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
@Data
public class RpcFuture<T> {
    private Promise<T> promise;
    private long timeout;

    public RpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
