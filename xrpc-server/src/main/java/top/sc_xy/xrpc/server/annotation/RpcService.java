package top.sc_xy.xrpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sc-xy
 * @time 2024/9/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {
    Class<?> serviceInterface() default Object.class;

    String serviceVersion() default "1.0";
}
