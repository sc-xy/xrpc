package top.sc_xy.xrpc.transport;

import top.sc_xy.xrpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 请求Id和对应结果相对应
 */
public class ResponseFuture {
    private int requestId;
    private CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<Command> future) {
        this.future = future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
