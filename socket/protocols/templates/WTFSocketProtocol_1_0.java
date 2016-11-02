package wtf.socket.protocols.templates;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import wtf.socket.annotations.Necessary;
import wtf.socket.annotations.Option;
import wtf.socket.annotations.Version;

/**
 * 协议V1.0
 */
@Version("1.0")
public class WTFSocketProtocol_1_0 extends WTFSocketProtocol {

    @Necessary
    private String from = "server";
    @Necessary
    private String to = "unknown";
    @Necessary
    private int msgId = 0;
    @Necessary
    private int msgType = 1;

    @Option
    private Integer flag = null;
    @Option
    private Integer errCode = null;
    @Option
    private Integer cmd = null;
    @Option
    private JSONArray params = null;

    @JSONField(serialize = false)
    private WTFSocketConnectType connectType = WTFSocketConnectType.TCP;

    public WTFSocketProtocol_1_0() {
        super(null);
    }

    public WTFSocketProtocol_1_0(WTFSocketProtocol protocol) {
        super(protocol);
        convert(protocol);
    }

    public static WTFSocketProtocol_1_0 makeResponse(WTFSocketProtocol protocol) {
        WTFSocketProtocol_1_0 protocol_1_0 = new WTFSocketProtocol_1_0();
        protocol_1_0.setFrom(protocol.getTo());
        protocol_1_0.setTo(protocol.getFrom());
        protocol_1_0.setMsgId(protocol.getMsgId());
        return protocol_1_0;
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
    @JSONField(serialize = false)
    public int getState() {
        return 0;
    }

    @Override
    @JSONField(serialize = false)
    public void setState(int state) {
        if (state != 1) {
            setErrCode(state);
        }
    }

    @Override
    @JSONField(serialize = false)
    public String getVersion() {
        return "1.0";
    }

    @Override
    @JSONField(serialize = false)
    public void setVersion(String version) {};

    public Integer getFlag() {
        return flag;
    }


    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public JSONArray getParams() {
        return params;
    }

    public void setParams(JSONArray params) {
        this.params = params;
    }

    @Override
    @JSONField(serialize = false)
    public JSONObject getBody() {
        JSONObject json = new JSONObject();
        json.put("version", "1.0");
        json.put("flag", flag);
        json.put("cmd", cmd);
        json.put("errCode", errCode);
        json.put("params", params);
        return json;
    }

    @Override
    @JSONField(serialize = false)
    public void setBody(JSONObject body) {
        if (body.containsKey("flag")) {
            flag = body.getInteger("flag");
        }
        if (body.containsKey("cmd")) {
            cmd = body.getInteger("cmd");
        }
        if (body.containsKey("errCode")) {
            errCode = body.getInteger("errCode");
        }
        if (body.containsKey("params")) {
            params = body.getJSONArray("params");
        }
    }

    @Override
    @JSONField(serialize = false)
    public <T> T getBody(Class<T> tClass) {
        return getBody().toJavaObject(tClass);
    }

    @Override
    @JSONField(serialize = false)
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
    public void setConnectType(WTFSocketConnectType type) {
        this.connectType = type;
    }
}
