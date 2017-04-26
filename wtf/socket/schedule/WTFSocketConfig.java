package wtf.socket.schedule;

public class WTFSocketConfig {

    private int tcpPort = 0;
    private int webSocketPort = 0;
    private boolean useDebug = false;
    private boolean cleanEmptyConnect = false;
    private boolean keepAlive = true;

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

    public boolean isCleanEmptyConnect() {
        return cleanEmptyConnect;
    }

    public void setCleanEmptyConnect(boolean cleanEmptyConnect) {
        this.cleanEmptyConnect = cleanEmptyConnect;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public WTFSocketConfig setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }
}
