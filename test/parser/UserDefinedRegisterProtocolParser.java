package parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import wtf.socket.exception.fatal.WTFSocketMsgFormatWrongException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.protocol.WTFSocketProtocolParser;
import wtf.socket.protocol.msg.WTFSocketDefaultMsg;

/**
 *
 * Created by zfly on 2017/4/23.
 */
public class UserDefinedRegisterProtocolParser implements WTFSocketProtocolParser {
    @Override
    public boolean isResponse(String data) {
        return StringUtils.startsWith(data,"I") && StringUtils.length(data) == 22;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return false;
    }

    @Override
    public WTFSocketMsg parseMsgFromString(String data) throws WTFSocketMsgFormatWrongException {
        String address;
        String deviceType;
        String version = data.substring(19, 22);

        switch (data.charAt(1)) {
            case 'H':
                deviceType = "Hardware";
                address = data.substring(7, 19);
                break;
            case 'A':
                deviceType = "Android";
                address = data.substring(8, 19);
                break;
            case 'I':
                deviceType = "IOS";
                address = data.substring(8, 19);
                break;
            case 'W':
                deviceType = "Web";
                address = data.substring(8, 19);
                break;
            default:
                deviceType = "Unknown";
                address = "Unknown";
                break;
        }

        return new WTFSocketDefaultMsg() {{
            setFrom(address);
            setTo("server");
            setVersion(version);
            setBody(new JSONObject() {{
                put("cmd", 64);
                put("params", new JSONArray() {{
                    add(new JSONObject() {{
                        put("deviceType", deviceType);
                    }});
                }});
            }});
        }};
    }
//    IHT****d8b04cb58f3c2.0\r\n
//    IAT****0185836873392.0\r\n
    @Override
    public String packageMsgToString(WTFSocketMsg msg) {
        return "";
    }
}
