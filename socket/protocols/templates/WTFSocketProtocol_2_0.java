package wtf.socket.protocols.templates;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import wtf.socket.annotations.Necessary;
import wtf.socket.annotations.Option;
import wtf.socket.annotations.Version;

/**
 * 协议V2.0
 */
@Version("2.0")
public class WTFSocketProtocol_2_0 extends WTFSocketProtocol {

    @Necessary
    private String from = "server";
    @Necessary
    private String to = "unknown";
    @Necessary
    private int msgId = 0;
    @Necessary
    private int msgType = 1;
    @Necessary
    private String version = "2.0";
    @Necessary
    private int state = 1;

    @Option
    private JSONObject body = null;

    @JSONField(serialize = false)
    private WTFSocketConnectType connectType = WTFSocketConnectType.TCP;

    public WTFSocketProtocol_2_0() {
        super(null);
    }

    public WTFSocketProtocol_2_0(WTFSocketProtocol protocol) {
        super(protocol);
        convert(protocol);
    }

    public static WTFSocketProtocol_2_0 makeResponse(WTFSocketProtocol protocol) {
        WTFSocketProtocol_2_0 wtfProtocol_2_0 = new WTFSocketProtocol_2_0();
        if (protocol != null) {
            wtfProtocol_2_0.setFrom(protocol.getTo());
            wtfProtocol_2_0.setTo(protocol.getFrom());
            wtfProtocol_2_0.setMsgId(protocol.getMsgId());
            wtfProtocol_2_0.setConnectType(protocol.getConnectType());
        }
        return wtfProtocol_2_0;
    }

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
    public void setBody(JSONObject body) {
        this.body = body;
    }

    @Override
    public <T> T getBody(Class<T> tClass) {
        return body.toJavaObject(tClass);
    }

    @Override
    public void setBody(Object body) {
        setBody((JSONObject) JSON.toJSON(body));
    }

    @Override
    @JSONField(serialize = false)
    public WTFSocketConnectType getConnectType() {
        return connectType;
    }

    @Override
    @JSONField(serialize = false)
    public void setConnectType(WTFSocketConnectType connectType) {
        this.connectType = connectType;
    }
}
