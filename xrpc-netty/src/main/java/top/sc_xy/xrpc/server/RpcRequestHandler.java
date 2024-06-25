package top.sc_xy.xrpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.client.ServerTypes;
import top.sc_xy.xrpc.client.stubs.RpcRequest;
import top.sc_xy.xrpc.serialize.SerializerSupport;
import top.sc_xy.xrpc.spi.Singleton;
import top.sc_xy.xrpc.transport.RequestHandler;
import top.sc_xy.xrpc.transport.command.Code;
import top.sc_xy.xrpc.transport.command.Command;
import top.sc_xy.xrpc.transport.command.Header;
import top.sc_xy.xrpc.transport.command.ResponseHeader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RpcRequestHandler implements ServerProviderRegister, RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private final Map<String/*服务名*/, Object/*服务提供者*/> serverProviders = new HashMap<>();

    @Override
    public <T> void addServerProvider(Class<? extends T> serverClass, T serverProvider) {
        serverProviders.put(serverClass.getCanonicalName(), serverProvider);
        logger.info("Add server: {}, provider: {}.",
                serverClass.getCanonicalName(),
                serverProvider.getClass().getCanonicalName());
    }

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();
        RpcRequest rpcRequest = SerializerSupport.parse(requestCommand.getPayload());
        try {
            Object serverProvider = serverProviders.get(rpcRequest.getInterfaceName());
            if (serverProvider != null) {
                String arg = SerializerSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serverProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serverProvider, arg);

                return new Command(new ResponseHeader(header.getRequestId(), header.getVersion(), type())
                        , SerializerSupport.serialize(result));
            } else {
                logger.warn("No such server provider for {}#{}(String)", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                return new Command(
                        new ResponseHeader(header.getRequestId(), header.getVersion(), type(), Code.NO_PROVIDER.getCode(), "No Provider!")
                        , new byte[0]);
            }
        } catch (Throwable t) {
            logger.warn("Exception: ", t);
            return new Command(
                    new ResponseHeader(header.getRequestId(), header.getVersion(), type(), t), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServerTypes.TYPE_RPC_REQUEST;
    }
}
