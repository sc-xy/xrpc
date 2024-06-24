package top.sc_xy.xrpc.serialize.impl;

import top.sc_xy.xrpc.client.stubs.RpcRequest;
import top.sc_xy.xrpc.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest request) {
        return Integer.BYTES + request.getInterfaceName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + request.getMethodName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + request.getSerializedArguments().length;
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] tmp = entry.getInterfaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmp.length);
        buffer.put(tmp);

        tmp = entry.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmp.length);
        buffer.put(tmp);

        tmp = entry.getSerializedArguments();
        buffer.putInt(tmp.length);
        buffer.put(tmp);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmp = new byte[len];
        buffer.get(tmp);
        String interfaceName = new String(tmp, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmp = new byte[len];
        buffer.get(tmp);
        String methodName = new String(tmp, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmp = new byte[len];
        byte[] argument = tmp;
        return new RpcRequest(interfaceName, methodName, argument);
    }


}
