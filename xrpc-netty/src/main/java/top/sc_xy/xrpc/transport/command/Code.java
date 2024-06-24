package top.sc_xy.xrpc.transport.command;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态信息枚举
 */
public enum Code {

    SUCCESS(0, "SUCCESS"),
    NO_PROVIDER(-1, "NO_PROVIDER"),
    UNKNOWN_ERROR(-7, "UNKNOWN_ERROR");
    private static Map<Integer, Code> map = new HashMap<>();

    static {
        for (Code code : Code.values()) {
            map.put(code.code, code);
        }
    }

    private int code;
    private String message;

    Code(int _code, String _message) {
        this.code = _code;
        this.message = _message;
    }

    public static Code valueOf(int code) {
        return map.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage(Object... args) {
        if (args.length < 1) {
            return this.message;
        }
        return String.format(this.message, args);
    }
}
