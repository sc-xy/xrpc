package top.sc_xy.xrpc.client;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestIdSupport {
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);
    public static int nextId() {
        return nextRequestId.getAndIncrement();
    }
}
