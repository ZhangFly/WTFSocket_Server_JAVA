package wtf.socket.protocol.msg;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import wtf.socket.exception.fatal.WTFSocketProtocolBrokenException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.protocol.WTFSocketProtocolParser;

/**
 * 默认协议解析器实现
 * 支持JSON格式，使用 fastjson 进行转换
 * 使用的实体类模板为 WTFSocketDefaultMsg
 *
 * Created by zfly on 2017/4/21.
 */
public class WTFSocketDefaultProtocolParser implements WTFSocketProtocolParser {

    public boolean isResponse(String data) {
        return StringUtils.startsWith(data, "{") && StringUtils.endsWith(data, "}");
    }

    public boolean isResponse(WTFSocketMsg msg) {
        return StringUtils.equals(msg.getVersion(), "2.0");
    }

    public WTFSocketMsg parseMsgFromString(String data) throws WTFSocketProtocolBrokenException {
        try {
            return  JSON.parseObject(data, WTFSocketDefaultMsg.class);
        }catch (Exception e) {
            throw new WTFSocketProtocolBrokenException(e.getMessage());
        }
    }

    public String packageMsgToString(WTFSocketMsg msg) {
        return JSON.toJSONString(msg);
    }
}

