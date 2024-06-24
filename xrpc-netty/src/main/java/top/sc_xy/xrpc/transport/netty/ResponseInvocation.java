package top.sc_xy.xrpc.transport.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.transport.InTransitRequests;
import top.sc_xy.xrpc.transport.ResponseFuture;
import top.sc_xy.xrpc.transport.command.Command;

@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
    private final InTransitRequests inTransitRequests;

    public ResponseInvocation(InTransitRequests inTransitRequests) {
        this.inTransitRequests = inTransitRequests;
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Command response) {
        ResponseFuture future = inTransitRequests.remove(response.getHeader().getRequestId());
        if (null != future) {
            future.getFuture().complete(response);
        } else {
            logger.warn("Drop response: {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        logger.warn("Exception: ", cause);
        super.exceptionCaught(channelHandlerContext, cause);
        Channel channel = channelHandlerContext.channel();
        if (channel.isActive()) {
            channelHandlerContext.close();
        }
    }
}
