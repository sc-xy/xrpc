package top.sc_xy.xrpc.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServerLoadSupport {
    private final static Map<String, Object> singletonServers = new HashMap<>();

    public synchronized static <E> E load(Class<E> serverClass) {
        return StreamSupport.
                stream(ServiceLoader.load(serverClass).spliterator(), false)
                .map(ServerLoadSupport::singletonFilter)
                .findFirst().orElseThrow(ServerLoadException::new);
    }

    public synchronized static <E> Collection<E> loadAll(Class<E> serverClass) {
        return StreamSupport.
                stream(ServiceLoader.load(serverClass).spliterator(), false)
                .map(ServerLoadSupport::singletonFilter).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <E> E singletonFilter(E serverClass) {
        if (serverClass.getClass().isAnnotationPresent(Singleton.class)) {
            String className = serverClass.getClass().getCanonicalName();
            Object singletonInstance = singletonServers.putIfAbsent(className, serverClass);
            return singletonInstance == null ? serverClass : (E) singletonInstance;
        } else {
            return serverClass;
        }
    }
}
