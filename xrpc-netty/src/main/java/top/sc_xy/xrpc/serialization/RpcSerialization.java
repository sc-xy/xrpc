package top.sc_xy.xrpc.serialization;

import java.io.IOException;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
