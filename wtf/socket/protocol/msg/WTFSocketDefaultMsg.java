package wtf.socket.protocol.msg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 默认协议实现类
 * 使用 fastjson 序列化
 *
 * Created by zfly on 2017/4/21.
 */
public class WTFSocketDefaultMsg implements WTFSocketMsg {

    private String ioTag = "0";
    private String from = "server";
    private String to = "unknown";
    private int msgId = 0;
    private int msgType = 1;
    private String version = "2.0";
    private int state = 1;
    private JSONObject body = null;
    private String connectType = "TCP";

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    @Override
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    @Override
    public int getMsgType() {
        return msgType;
    }

    @Override
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public JSONObject getBody() {
        return body;
    }

    @Override
    @JSONField(serialize = false)
    public <T> T getBody(Class<T> tClass) {
        return body.toJavaObject(tClass);
    }

    @Override
    @JSONField(serialize = false)
    public void setBody(Object body) {
        this.body = ((JSONObject) JSON.toJSON(body));
    }

    @Override
    @JSONField(serialize = false)
    public String getConnectType() {
        return connectType;
    }

    @Override
    @JSONField(serialize = false)
    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    @Override
    @JSONField(serialize = false)
    public WTFSocketMsg makeResponse() {
        WTFSocketDefaultMsg response = new WTFSocketDefaultMsg();
        response.setFrom(getTo());
        response.setTo(getFrom());
        response.setMsgId(getMsgId());
        response.setConnectType(getConnectType());
        response.setVersion(getVersion());
        return response;
    }

    @Override
    @JSONField(serialize = false)
    public String getIoTag() {
        return ioTag;
    }

    @Override
    @JSONField(serialize = false)
    public void setIoTag(String ioTag) {
        this.ioTag = ioTag;
    }
}
