package top.sc_xy.xrpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
@Data
public class RpcResponse implements Serializable {
    private Object data;
    private String message;
}
