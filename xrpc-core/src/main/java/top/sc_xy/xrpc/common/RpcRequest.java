package top.sc_xy.xrpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
@Data
public class RpcRequest implements Serializable {
    private String serviceVersion;
    private String className;
    private String methodName;
    private Object[] params;
    private Class<?>[] parameterTypes;
}
