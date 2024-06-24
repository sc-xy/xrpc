package top.sc_xy.xrpc.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 存储已发送但未收到相应的请求
 */
public class InTransitRequests implements Closeable {
    /**
     * 响应超时时间
     */
    private final static long TIMEOUT_SEC = 10L;
    /**
     * 简单背压机制，防止太多请求堆积导致OOM
     */
    private final Semaphore semaphore = new Semaphore(10);
    /**
     * 将requestId与响应一一对应
     */
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final ScheduledFuture scheduledFuture;

    public InTransitRequests() {
        this.scheduledFuture = scheduledExecutorService.schedule(this::removeTimeoutFutures, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(
                entry -> {
                    if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                        semaphore.release();
                        return true;
                    } else {
                        return false;
                    }
                }
        );
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.get(requestId);
        if (null != future) {
            semaphore.release();
        }
        return future;
    }

    @Override
    public void close() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}

