package top.sc_xy.xrpc.client;

import com.itranswarp.compiler.JavaStringCompiler;
import top.sc_xy.xrpc.transport.Transport;

import java.util.Map;

public class DynamicStubFactory implements StubFactory {
    private static final String STUB_TEMPLATE =
            "package top.sc_xy.xrpc.client.stubs;\n" +
            "import top.sc_xy.xrpc.serialize.SerializerSupport;\n" +
            "\n" +
            "public class %s extends AbstractStub implements %s {\n" +
            "    @Override\n" +
            "    public String %s(String arg) {\n" +
            "        return SerializerSupport.parse(\n" +
            "                invokeRemote(\n" +
            "                        new RpcRequest(\n" +
            "                                \"%s\",\n" +
            "                                \"%s\",\n" +
            "                                SerializerSupport.serialize(arg)\n" +
            "                        )\n" +
            "                )\n" +
            "        );\n" +
            "    }\n" +
            "}";
    @Override
    public <T> T createStub(Transport transport, Class<T> serverClass) {
        try {
            String stubSimpleName = serverClass.getSimpleName() + "Stub";
            String classFullName = serverClass.getName();
            String stubFullName = "top.sc_xy.xrpc.client.stubs." + stubSimpleName;
            String methodName = serverClass.getMethods()[0].getName();

            String sourceCode = String.format(STUB_TEMPLATE,
                    stubSimpleName, classFullName, methodName, classFullName, methodName);

            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", sourceCode);

            Class<?> clazz = compiler.loadClass(stubFullName, results);

            ServerStub stubInstance = (ServerStub) clazz.newInstance();
            stubInstance.setTransport(transport);

            return (T) stubInstance;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
