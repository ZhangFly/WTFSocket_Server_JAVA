package wtf.socket.protocol;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息接口
 * 所以自定义消息类型需要实现该接口
 *
 * Created by zfly on 2017/4/21.
 */
public interface WTFSocketMsg {

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

    void setConnectType(WTFSocketConnectType connectType);

    WTFSocketConnectType getConnectType();

    String getVersion();

    void setVersion(String version);

    JSONObject getBody();

    <T> T getBody(Class<T> tClass);

    void setBody(JSONObject body);

    void setBody(Object body);

    WTFSocketMsg makeResponse();
}
