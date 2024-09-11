package top.sc_xy.xrpc.protocol;

import lombok.Getter;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
public enum MsgType {
    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);

    @Getter
    private final int type;

    MsgType(int type) {
        this.type = type;
    }

    public static MsgType findByType(int type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }
}
