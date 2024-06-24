package top.sc_xy.xrpc.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.sc_xy.xrpc.transport.command.Command;
import top.sc_xy.xrpc.transport.command.Header;

public class CommandEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (!(o instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s!", o.getClass().getCanonicalName()));
        }

        Command command = (Command) o;
        byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);

        encodeHeader(channelHandlerContext, command.getHeader(), byteBuf);
        byteBuf.writeBytes(command.getPayload());
    }

    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(header.getRequestId());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getType());
    }
}
