package top.sc_xy.xrpc;

import top.sc_xy.xrpc.client.StubFactory;
import top.sc_xy.xrpc.server.ServerProviderRegister;
import top.sc_xy.xrpc.spi.ServerLoadSupport;
import top.sc_xy.xrpc.transport.RequestHandlerRegister;
import top.sc_xy.xrpc.transport.Transport;
import top.sc_xy.xrpc.transport.TransportClient;
import top.sc_xy.xrpc.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class NettyXrpcPoint implements XrpcPoint {

    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);

    private TransportServer server = null;
    private TransportClient client = ServerLoadSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServerLoadSupport.load(StubFactory.class);
    private final ServerProviderRegister serverProviderRegister = ServerLoadSupport.load(ServerProviderRegister.class);

    @Override
    public <T> T getRemoteServer(URI uri, Class<T> serverClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serverClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI registerServerProvider(T server, Class<T> serverClass) {
        serverProviderRegister.addServerProvider(serverClass, server);
        return uri;
    }

    @Override
    public synchronized Closeable start() throws Exception {
        if (null == server) {
            server = ServerLoadSupport.load(TransportServer.class);
            server.start(RequestHandlerRegister.getInstance(), port);

        }
        return () -> {
            if (null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (null != server) {
            server.stop();
        }
        client.close();
    }
}