package top.sc_xy.xrpc.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SerializerSupport {
    private static final Logger logger = LoggerFactory.getLogger(SerializerSupport.class);
    private static Map<Class<?> /*序列化对象类型*/, Serializer<?> /*序列化实现*/> serializerMap = new HashMap<>();
    private static Map<Byte /*序列化实现类型*/, Class<?> /*序列化类型*/> typeMap = new HashMap<>();

    static {
        for (Serializer serializer : ServerLoadSupport.loadAll(Serializer.class)) {
            registerType(serializer.type(), serializer.getSerializeClass(), serializer);
            logger.info("Found serializer, class: {}, type: {}.",
                    serializer.getSerializeClass().getCanonicalName(),
                    serializer.type());
        }
    }

    private static byte parseEntryType(byte[] buffer) {
        return buffer[0];
    }

    private static <E> void registerType(byte type, Class<E> eClass, Serializer<E> eSerializer) {
        typeMap.put(type, eClass);
        serializerMap.put(eClass, eSerializer);
    }

    private static <E> E parse(Class<E> eClass, byte[] buffer, int offset, int length) {
        if (serializerMap.containsKey(eClass)) {
            Object entry = serializerMap.get(eClass).parse(buffer, offset, length);
            if (eClass.isAssignableFrom(entry.getClass())) {
                return (E) entry;
            } else {
                throw new SerializeException("Type mismatch!");
            }
        } else {
            throw new SerializeException("No corresponding Serializer!");
        }
    }

    private static <E> E parse(byte[] buffer, int offset, int length) {
        byte type = parseEntryType(buffer);
        if (typeMap.containsKey(type)) {
            Class<E> eClass = (Class<E>) typeMap.get(type);
            return parse(eClass, buffer, offset + 1, length - 1);
        } else {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        }
    }

    public static <E> E parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    public static <E> byte[] serialize(E entry) {
        Class<E> eClass = (Class<E>) entry.getClass();
        if (serializerMap.containsKey(eClass)) {
            Serializer<E> serializer = (Serializer<E>) serializerMap.get(eClass);
            byte[] buffer = new byte[serializer.size(entry) + 1];
            buffer[0] = serializer.type();
            serializer.serialize(entry, buffer, 1, buffer.length - 1);
            return buffer;
        } else {
            throw new SerializeException(String.format("No corresponding Serializer for entry class: %s!", eClass));
        }
    }
}
