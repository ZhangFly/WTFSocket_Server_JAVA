package wtf.socket.protocol.msg;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import wtf.socket.exception.fatal.WTFSocketProtocolBrokenException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.protocol.WTFSocketProtocolParser;
import wtf.socket.util.WTFSocketPriority;

/**
 * 默认协议解析器实现
 * 支持 JSON 格式，使用 FastJson 进行转换
 * 使用的实体类模板为 WTFSocketDefaultMsg
 * 优先级为 WTFSocketPriority.LOWEST
 * <p>
 * Created by ZFly on 2017/4/21.
 */
public class WTFSocketDefaultProtocolParser implements WTFSocketProtocolParser {


    @Override
    public int getPriority() {
        return WTFSocketPriority.LOWEST;
    }

    @Override
    public boolean isResponse(String data) {
        return StringUtils.startsWith(data, "{") && StringUtils.endsWith(data, "}");
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return StringUtils.equals(msg.getVersion(), "2.0");
    }

    @Override
    public WTFSocketMsg parse(String data) throws WTFSocketProtocolBrokenException {
        try {
            return JSON.parseObject(data, WTFSocketDefaultMsg.class);
        } catch (Exception e) {
            throw new WTFSocketProtocolBrokenException(e.getMessage());
        }
    }

    @Override
    public String parse(WTFSocketMsg msg) {
        return JSON.toJSONString(msg);
    }
}

