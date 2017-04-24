package wtf.socket.schedule;

public class WTFSocketConfig {

    private int tcpPort = -1;
    private int webSocketPort = -1;
    private boolean useDebug = false;

    public int getTcpPort() {
        return tcpPort;
    }

    public WTFSocketConfig setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
        return this;
    }

    public int getWebSocketPort() {
        return webSocketPort;
    }

    public WTFSocketConfig setWebSocketPort(int webSocketPort) {
        this.webSocketPort = webSocketPort;
        return this;
    }

    public boolean isUseDebug() {
        return useDebug;
    }

    public WTFSocketConfig setUseDebug(boolean useDebug) {
        this.useDebug = useDebug;
        return this;
    }
}
