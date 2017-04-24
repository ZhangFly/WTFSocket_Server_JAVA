package wtf.socket.protocol;

/**
 * 连接类型枚举
 */
public enum WTFSocketConnectType {

    TCP("TCP"),
    WebSocket("WebSocket");

    private final String name;

    WTFSocketConnectType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
