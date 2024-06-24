package top.sc_xy.xrpc.transport.command;

import java.nio.charset.StandardCharsets;

/**
 * 相应的请求头
 */
public class ResponseHeader extends Header {
    private int code;
    private String error;

    public ResponseHeader(int requestId, int version, int type, int code, String error) {
        super(requestId, version, type);
        this.code = code;
        this.error = error;
    }

    public ResponseHeader(int requestId, int version, int type, Throwable throwable) {
        this(requestId, version, type, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(int requestId, int version, int type) {
        this(requestId, version, type, Code.SUCCESS.getCode(), null);
    }

    @Override
    public int length() {
        return super.length() + Integer.BYTES + Integer.BYTES +
                (error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
