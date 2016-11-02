package wtf.socket.protocols.templates;

import com.alibaba.fastjson.JSONObject;

/**
 * 协议接口
 */
public abstract class WTFSocketProtocol {

    WTFSocketProtocol(WTFSocketProtocol protocol) {

    }

    void convert(WTFSocketProtocol protocol) {
        setFrom(protocol.getFrom());
        setTo(protocol.getTo());
        setMsgId(protocol.getMsgId());
        setMsgType(protocol.getMsgType());
        setBody(protocol.getBody());
        setState(protocol.getState());
        setVersion(protocol.getVersion());
        setConnectType(protocol.getConnectType());
    }

    public abstract String getFrom();

    public abstract void setFrom(String from);

    public abstract String getTo();

    public abstract void setTo(String to);

    public abstract int getMsgId();

    public abstract void setMsgId(int msgId);

    public abstract int getMsgType();

    public abstract void setMsgType(int msgType);

    public abstract int getState();

    public abstract void setState(int state);

    public abstract void setConnectType(WTFSocketConnectType connectType);

    public abstract WTFSocketConnectType getConnectType();

    public abstract String getVersion();

    public abstract void setVersion(String version);

    public abstract JSONObject getBody();

    public abstract void setBody(JSONObject body);

    public abstract <T> T getBody(Class<T> tClass);

    public abstract void setBody(Object body);
}
