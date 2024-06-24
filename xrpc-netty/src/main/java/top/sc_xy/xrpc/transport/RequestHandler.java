package top.sc_xy.xrpc.transport;

import top.sc_xy.xrpc.transport.command.Command;

public interface RequestHandler {
    /**
     * 处理请求
     *
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand);

    /**
     * 处理的请求类型
     **/
    int type();
}
