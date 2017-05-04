package wtf.socket.event;

/**
 * 事件类型
 * <p>
 * Created by ZFly on 2017/4/26.
 */
public enum WTFSocketEventsType {
    /**
     * 客户端断开连接时触发
     */
    Disconnect,
    /**
     * 有新客户端连接时触发
     */
    Connect,
    /**
     * 服务器启动时触发
     */
    ServerStarted
}
