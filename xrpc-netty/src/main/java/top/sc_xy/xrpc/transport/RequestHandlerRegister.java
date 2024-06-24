package top.sc_xy.xrpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RequestHandlerRegister {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRegister.class);
    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    private static volatile RequestHandlerRegister instance;

    public static RequestHandlerRegister getInstance() {
        if (instance == null) {
            synchronized (RequestHandlerRegister.class) {
                if (instance == null) {
                    instance = new RequestHandlerRegister();
                }
            }
        }
        return instance;
    }

    private RequestHandlerRegister() {
        Collection<RequestHandler> requestHandlers = ServerLoadSupport.loadAll(RequestHandler.class);
        for (RequestHandler requestHandler : requestHandlers) {
            handlerMap.put(requestHandler.type(), requestHandler);
            logger.info("Load request handler, type: {}, class: {}.",
                    requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }

}
