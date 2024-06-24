package top.sc_xy.xrpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.HelloService;
import top.sc_xy.xrpc.NameServer;
import top.sc_xy.xrpc.XrpcPoint;
import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String [] args) throws Exception {

        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File("C:\\Users\\XY\\Desktop\\temp");
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动RpcAccessPoint...");
        try(XrpcPoint rpcAccessPoint = ServerLoadSupport.load(XrpcPoint.class);
            Closeable ignored = rpcAccessPoint.start()) {
            NameServer nameService = rpcAccessPoint.getNameServer(file.toURI());
            assert nameService != null;
            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            URI uri = rpcAccessPoint.registerServerProvider(helloService, HelloService.class);
            logger.info("服务名: {}, 向NameService注册...", serviceName);
            nameService.registerServer(serviceName, uri);
            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }
}