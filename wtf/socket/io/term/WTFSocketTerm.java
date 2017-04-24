package wtf.socket.io.term;

import wtf.socket.protocol.WTFSocketConnectType;

/**
 * 终端接口
 *
 * Created by zfly on 2017/4/22.
 */
public interface WTFSocketTerm {

    /**
     * 获取io标签，
     * io标签与实际的一条链路对应
     * 终端每次连接服务器应该产生一个新的io标签
     *
     * @return io标签
     */
    String getIoTag();

    /**
     * 设置io标签，
     * io标c签与实际的一条链路对应
     * 终端每次连接服务器应该产生一个新的io标签
     *
     * @param ioTag io标签
     */
    void setIoTag(String ioTag);

    /**
     * 获取连接类型
     *
     * @return 连接类型
     */
    WTFSocketConnectType getConnectType();

    /**
     * 设置连接类型
     *
     * @param connectType 连接类型
     */
    void setConnectType(WTFSocketConnectType connectType);

    /**
     * 向终端写数据
     *
     * @param data 数据
     */
    void write(String data);

    /**
     * 关闭与终端的连接
     */
    void close();
}
