package top.sc_xy.xrpc.server.impl;

import top.sc_xy.xrpc.HelloService;
import top.sc_xy.xrpc.server.annotation.RpcService;

/**
 * @author sc-xy
 * @time 2024/9/12
 */
@RpcService(serviceInterface = HelloService.class, serviceVersion = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
