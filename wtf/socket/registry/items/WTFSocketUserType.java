package wtf.socket.registry.items;

public enum WTFSocketUserType {

    USER("User"),
    DEBUG("Debug"),
    TMP("Tmp");

    final String type;

    WTFSocketUserType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}
