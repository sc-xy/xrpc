package top.sc_xy.xrpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.sc_xy.xrpc.HelloService;
import top.sc_xy.xrpc.NameServer;
import top.sc_xy.xrpc.XrpcPoint;
import top.sc_xy.xrpc.spi.ServerLoadSupport;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String [] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File("C:\\Users\\XY\\Desktop\\temp");
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        String name = "Master MQ";
        try(XrpcPoint rpcAccessPoint = ServerLoadSupport.load(XrpcPoint.class)) {
            NameServer nameService = rpcAccessPoint.getNameServer(file.toURI());
            assert nameService != null;
            URI uri = nameService.findServer(serviceName);
            assert uri != null;
            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            HelloService helloService = rpcAccessPoint.getRemoteServer(uri, HelloService.class);
            logger.info("请求服务, name: {}...", name);
            String response = helloService.hello(name);
            logger.info("收到响应: {}.", response);
        }


    }
}