package wtf.socket.protocols.templates;

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
