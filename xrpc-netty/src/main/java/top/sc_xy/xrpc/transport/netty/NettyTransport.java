package top.sc_xy.xrpc.transport.netty;

import io.netty.channel.ChannelFutureListener;
import top.sc_xy.xrpc.transport.InTransitRequests;
import top.sc_xy.xrpc.transport.ResponseFuture;
import top.sc_xy.xrpc.transport.Transport;
import top.sc_xy.xrpc.transport.command.Command;

import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

public class NettyTransport implements Transport {
    private final Channel channel;
    private final InTransitRequests inTransitRequests;

    public NettyTransport(Channel channel, InTransitRequests inTransitRequests) {
        this.channel = channel;
        this.inTransitRequests = inTransitRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            inTransitRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            channel.writeAndFlush(request).addListener(
                    (ChannelFutureListener) channelFuture -> {
                        if (!channelFuture.isSuccess()) {
                            completableFuture.completeExceptionally(channelFuture.cause());
                            channel.close();
                        }
                    }
            );
        } catch (Throwable t) {
            inTransitRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }

    ;

}
