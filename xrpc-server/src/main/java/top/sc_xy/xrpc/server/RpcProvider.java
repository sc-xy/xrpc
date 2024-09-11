package top.sc_xy.xrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import top.sc_xy.xrpc.codec.RpcDecoder;
import top.sc_xy.xrpc.codec.RpcEncoder;
import top.sc_xy.xrpc.common.RpcServiceHelper;
import top.sc_xy.xrpc.common.ServiceMeta;
import top.sc_xy.xrpc.handler.RpcRequestHandler;
import top.sc_xy.xrpc.registry.RegistryService;
import top.sc_xy.xrpc.server.annotation.RpcService;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sc-xy
 * @time 2024/9/9
 */
@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor {
    private String serverAddress;
    private final int serverPort;
    private final RegistryService serviceRegistry;

    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcProvider(int serverPort, RegistryService serviceRegistry) {
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (Exception e) {
                log.error("start rpc server error.", e);
            }
        }).start();
    }

    public void startRpcServer() throws Exception {
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.serverAddress, this.serverPort).sync();
            log.info("server addr {} started on port {}", this.serverAddress, this.serverPort);
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();
            rpcServiceMap.put(serviceName + serviceVersion, bean);
            try {
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setServiceAddr(serverAddress);
                serviceMeta.setServicePort(serverPort);
                serviceMeta.setServiceName(serviceName);
                serviceMeta.setServiceVersion(serviceVersion);

                serviceRegistry.register(serviceMeta);
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()), bean);
                log.info("register service: {} version: {} on port: {}", serviceName, serviceVersion, serverPort);
            } catch (Exception e) {
                log.error("failed to register service {}#{}", serviceName, serviceVersion, e);
            }
        }
        return bean;
    }
}
