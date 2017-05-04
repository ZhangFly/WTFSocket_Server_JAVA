package wtf.socket.protocol;

/**
 * 消息接口
 * 所以自定义消息类型需要实现该接口
 * <p>
 * Created by ZFly on 2017/4/21.
 */
public interface WTFSocketMessage {

    String getIoTag();

    void setIoTag(String ioTag);

    String getFrom();

    void setFrom(String from);

    String getTo();

    void setTo(String to);

    int getMsgId();

    void setMsgId(int msgId);

    int getMsgType();

    void setMsgType(int msgType);

    int getState();

    void setState(int state);

    void setConnectType(String connectType);

    String getConnectType();

    String getVersion();

    void setVersion(String version);

    Object getBody();

    <T> T getBody(Class<T> tClass);

    void setBody(Object body);

    WTFSocketMessage makeResponse();
}
