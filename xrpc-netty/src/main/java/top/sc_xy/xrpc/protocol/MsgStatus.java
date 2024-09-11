package top.sc_xy.xrpc.protocol;

import lombok.Getter;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
public enum MsgStatus {
    SUCCESS(0),
    FAIL(1);

    @Getter
    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }
}
