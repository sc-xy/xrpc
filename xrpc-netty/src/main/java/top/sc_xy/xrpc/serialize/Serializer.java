package top.sc_xy.xrpc.serialize;

/**
 * 自定义序列化实现
 *
 * @param <T>
 */
public interface Serializer<T> {
    /**
     * 计算对象序列化后的数据长度
     *
     * @param entry 序列化对象
     * @return 序列化后的数据长度
     */
    int size(T entry);

    /**
     * 序列化对象，将对象转化为字节数组
     *
     * @param entry  待序列化的对象
     * @param bytes  存放序列化数据的数组
     * @param offset 数组偏移量，从这个位置开始写入序列化数据
     * @param length 对象序列化后的长度，也就是{@link Serializer#size(Object)}的返回值
     */
    void serialize(T entry, byte[] bytes, int offset, int length);

    /**
     * 从字节数组反序列化对象
     *
     * @param bytes  存放序列化数据的数组
     * @param offset 数组偏移量，从这个位置开始写入序列化数据
     * @param length 对象序列化后的长度，也就是{@link Serializer#size(Object)}的返回值
     * @return 反序列化后的对象
     */
    T parse(byte[] bytes, int offset, int length);

    /**
     * 用一个字节标记数据的类型
     */
    byte type();

    /**
     * 返回序列化对象类型的 Class
     */
    Class<T> getSerializeClass();
}
