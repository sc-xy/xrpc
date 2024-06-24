package top.sc_xy.xrpc.transport.command;

/**
 * 请求的请求头
 */
public class Header {
    private int requestId;
    private int version;
    private int type;

    public Header(int _requestId, int _version, int _type) {
        this.requestId = _requestId;
        this.version = _version;
        this.type = _type;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int length() {
        return Integer.BYTES * 3;
    }
}
