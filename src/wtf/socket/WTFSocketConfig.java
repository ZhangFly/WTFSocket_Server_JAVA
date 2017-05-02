package wtf.socket;

import java.util.ArrayList;
import java.util.List;

public class WTFSocketConfig {

    private int tcpPort = 0;
    private int webSocketPort = 0;
    private boolean useDebug = false;
    private boolean cleanEmptyConnect = false;
    private boolean keepAlive = true;
    private boolean useMsgForward = true;
    private List<String> EOTs = new ArrayList<String>() {{
        add("\r\n");
    }};
    private String springPath = "spring.xml";

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

    public void cleanEmptyConnect(boolean cleanEmptyConnect) {
        this.cleanEmptyConnect = cleanEmptyConnect;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public WTFSocketConfig keepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public boolean isUseMsgForward() {
        return useMsgForward;
    }

    public void useMsgForward(boolean useMsgForward) {
        this.useMsgForward = useMsgForward;
    }

    public List<String> getEOTs() {
        return EOTs;
    }

    public WTFSocketConfig addEOT(String EOT) {
       EOTs.add(EOT);
       return this;
    }

    public String getFirstEOT() {
        return EOTs.get(0);
    }

    public String getSpringPath() {
        return springPath;
    }

    public void setSpringPath(String springPath) {
        this.springPath = springPath;
    }
}
