package top.sc_xy.xrpc.serialize;

public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable t) {
        super(t);
    }
}
