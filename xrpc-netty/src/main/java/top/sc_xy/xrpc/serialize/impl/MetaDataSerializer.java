package top.sc_xy.xrpc.serialize.impl;

import top.sc_xy.xrpc.nameServer.MetaData;
import top.sc_xy.xrpc.serialize.Serializer;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetaDataSerializer implements Serializer<MetaData> {

    @Override
    public int size(MetaData entry) {
        return Short.BYTES +                   // Size of the map                  2 bytes
                entry.entrySet().stream()
                        .mapToInt(this::entrySize).sum();
    }

    @Override
    public void serialize(MetaData entry, byte[] bytes, int offset, int length) {

        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putShort(toShortSafely(entry.size()));

        entry.forEach((k, v) -> {
            byte[] keyBytes = k.getBytes(StandardCharsets.UTF_8);
            buffer.putShort(toShortSafely(keyBytes.length));
            buffer.put(keyBytes);

            buffer.putShort(toShortSafely(v.size()));
            for (URI uri : v) {
                byte[] uriBytes = uri.toASCIIString().getBytes(StandardCharsets.UTF_8);
                buffer.putShort(toShortSafely(uriBytes.length));
                buffer.put(uriBytes);
            }

        });
    }

    private int entrySize(Map.Entry<String, List<URI>> e) {
        // Map entry:
        return Short.BYTES +       // Key string length:               2 bytes
                e.getKey().getBytes().length +    // Serialized key bytes:   variable length
                Short.BYTES + // List size:              2 bytes
                e.getValue().stream() // Value list
                        .mapToInt(uri -> {
                            return Short.BYTES +       // Key string length:               2 bytes
                                    uri.toASCIIString().getBytes(StandardCharsets.UTF_8).length;    // Serialized key bytes:   variable length
                        }).sum();
    }

    @Override
    public MetaData parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        MetaData metadata = new MetaData();
        int sizeOfMap = buffer.getShort();
        for (int i = 0; i < sizeOfMap; i++) {
            int keyLength = buffer.getShort();
            byte[] keyBytes = new byte[keyLength];
            buffer.get(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);


            int uriListSize = buffer.getShort();
            List<URI> uriList = new ArrayList<>(uriListSize);
            for (int j = 0; j < uriListSize; j++) {
                int uriLength = buffer.getShort();
                byte[] uriBytes = new byte[uriLength];
                buffer.get(uriBytes);
                URI uri = URI.create(new String(uriBytes, StandardCharsets.UTF_8));
                uriList.add(uri);
            }
            metadata.put(key, uriList);
        }
        return metadata;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<MetaData> getSerializeClass() {
        return MetaData.class;
    }

    private short toShortSafely(int v) {
        assert v < Short.MAX_VALUE;
        return (short) v;
    }
}