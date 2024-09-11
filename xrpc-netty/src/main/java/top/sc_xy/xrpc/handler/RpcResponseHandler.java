package top.sc_xy.xrpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.sc_xy.xrpc.common.RpcFuture;
import top.sc_xy.xrpc.common.RpcRequestHolder;
import top.sc_xy.xrpc.common.RpcResponse;
import top.sc_xy.xrpc.protocol.RpcProtocol;

/**
 * @author sc-xy
 * @time 2024/9/12
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) {
        long requestId = msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }
}
