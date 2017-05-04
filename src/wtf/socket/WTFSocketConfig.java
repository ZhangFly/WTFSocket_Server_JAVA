package wtf.socket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WTFSocketConfig {

    private int tcpPort = 0;
    private int webSocketPort = 0;
    private boolean debug = false;
    private boolean cleanEmptyConnect = false;
    private boolean keepAlive = true;
    private boolean msgForward = true;
    private List<String> EOTs = new ArrayList<String>() {{
        add("\r\n");
    }};

    public static WTFSocketConfig createFromProperties(Properties configProperties) {
        final WTFSocketConfig config = new WTFSocketConfig();

        if (configProperties.containsKey("tcpPort")) {
            config.setTcpPort(Integer.valueOf(configProperties.getProperty("tcpPort")));
        }
        if (configProperties.containsKey("webSocketPort")) {
            config.setWebSocketPort(Integer.valueOf(configProperties.getProperty("webSocketPort")));
        }
        if (configProperties.containsKey("debug")) {
            config.setDebug(Boolean.parseBoolean(configProperties.getProperty("debug")));
        }
        if (configProperties.containsKey("cleanEmptyConnect")) {
            config.setCleanEmptyConnect(Boolean.parseBoolean(configProperties.getProperty("cleanEmptyConnect")));
        }
        if (configProperties.containsKey("keepAlive")) {
            config.setKeepAlive(Boolean.parseBoolean(configProperties.getProperty("keepAlive")));
        }
        if (configProperties.containsKey("msgForward")) {
            config.setMsgForward(Boolean.parseBoolean(configProperties.getProperty("msgForward")));
        }
        if (configProperties.containsKey("EOTs")) {
            String[] EOTs = configProperties.getProperty("EOTs").split(",");
            Arrays.stream(EOTs).forEach(config::addEOT);
        }
        return config;
    }

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

    public boolean isDebug() {
        return debug;
    }

    public WTFSocketConfig setDebug(boolean debug) {
        this.debug = debug;
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

    public boolean isMsgForward() {
        return msgForward;
    }

    public void setMsgForward(boolean msgForward) {
        this.msgForward = msgForward;
    }

    public List<String> getEOTs() {
        return EOTs;
    }

    public WTFSocketConfig addEOT(String EOT) {
        EOTs.add(EOT);
        return this;
    }

    @JSONField(serialize = false)
    public String getFirstEOT() {
        return EOTs.get(0);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
