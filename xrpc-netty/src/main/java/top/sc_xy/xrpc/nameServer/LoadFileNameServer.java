package top.sc_xy.xrpc.nameServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.NameServer;
import top.sc_xy.xrpc.serialize.SerializerSupport;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoadFileNameServer implements NameServer {
    private static final Logger logger = LoggerFactory.getLogger(LoadFileNameServer.class);
    private static final Collection<String> schemes = Collections.singleton("file");
    private File file;

    @Override
    public synchronized void registerServer(String serverName, URI uri) throws IOException {
        logger.info("Register server: {}, uri: {}", serverName, uri);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                int fileLength = (int) raf.length();
                MetaData metaData;
                byte[] bytes;
                if (fileLength > 0) {
                    bytes = new byte[fileLength];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }

                    metaData = SerializerSupport.parse(bytes);
                } else {
                    metaData = new MetaData();
                }
                List<URI> uris = metaData.computeIfAbsent(serverName, k -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
                }
                logger.info(metaData.toString());

                bytes = SerializerSupport.serialize(metaData);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public URI findServer(String serverName) throws IOException {
        MetaData metaData;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                metaData = bytes.length == 0 ? new MetaData() : SerializerSupport.parse(bytes);
            } finally {
                lock.release();
            }
        }
        List<URI> uris = metaData.get(serverName);
        if (uris == null || uris.isEmpty()) {
            return null;
        }
        return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
    }

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServerUri) {
        if (schemes.contains(nameServerUri.getScheme())) {
            file = new File(nameServerUri);
        } else {
            throw new RuntimeException("Unsupported Scheme!");
        }
    }
}
