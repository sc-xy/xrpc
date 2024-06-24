package top.sc_xy.xrpc.transport;

import top.sc_xy.xrpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 通信模板
 */
public interface Transport {
    /**
     * 发送请求命令
     * @param command 请求命令
     * @return 异步线程
     */
    CompletableFuture<Command> send(Command command);
}
