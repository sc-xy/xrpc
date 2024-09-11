package top.sc_xy.xrpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private MsgHeader header;
    private T body;
}
