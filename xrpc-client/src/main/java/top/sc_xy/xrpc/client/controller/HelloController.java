package top.sc_xy.xrpc.client.controller;

import org.springframework.web.bind.annotation.*;
import top.sc_xy.xrpc.HelloService;
import top.sc_xy.xrpc.client.annotation.RpcReference;

@RestController
public class HelloController {

    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @RpcReference(serviceVersion = "1.0.0", timeout = 3000)
    private HelloService helloFacade;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello(@RequestParam("name") String name) {
        return helloFacade.hello(name);
    }
}
