package top.sc_xy.xrpc.serialization;

/**
 * @author sc-xy
 * @time 2024/9/11
 */
public class SerializationException extends RuntimeException {
    private static final long serialVersionUID = 3365624081242234230L;

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
