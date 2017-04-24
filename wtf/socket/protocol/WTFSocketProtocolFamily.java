package wtf.socket.protocol;

import wtf.socket.exception.WTFSocketMsgFormatWrongException;
import wtf.socket.exception.WTFSocketUnsupportedProtocolException;
import wtf.socket.protocol.impl.WTFSocketDefaultParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议簇，通过向协议簇注册解析器
 * 将不同协议的数据包统一转化到 WTFSocketMsg
 *
 * Created by zfly on 2017/4/21.
 */
public abstract class WTFSocketProtocolFamily {

    /**
     * 解析器列表
     */
    private static List<WTFSocketParser> parsers = new ArrayList<WTFSocketParser>() {{
        add(new WTFSocketDefaultParser());
    }};

    /**
     * 选择解析器从字符串数据中解析出消息对象
     *
     * @param data 字符串数据
     * @return 消息对象
     * @throws WTFSocketMsgFormatWrongException 消息格式错误
     * @throws WTFSocketUnsupportedProtocolException 没有适合的解析器
     */
    public static WTFSocketMsg parseMsgFromString(String data) throws WTFSocketMsgFormatWrongException, WTFSocketUnsupportedProtocolException {
        for (WTFSocketParser parser : parsers) {
            if (parser.isResponse(data)) {
                return parser.parseMsgFromString(data);
            }
        }
        throw new WTFSocketUnsupportedProtocolException(data);
    }

    /**
     * 选择解析器将消息对象打包为字符串数据
     *
     * @param msg 消息对象
     * @return 字符串数据
     */
    public static String packageMsgToString(WTFSocketMsg msg) {
        for (WTFSocketParser parser : parsers) {
            if (parser.isResponse(msg)) {
                return parser.packageMsgToString(msg);
            }
        }
        return "Unsupported message";
    }

    /**
     * 注册解析器
     *
     * @param parser 要解析器对象
     */
    public static void registerParser(WTFSocketParser parser) {
        parsers.add(parser);
    }

    /**
     * 注销解析器
     *
     * @param parser 要解析器对象
     */
    public static void unregisterParser(WTFSocketParser parser) {
        parsers.remove(parser);
    }

    /**
     * 清空所以解析器
     */
    public static void clearParsers() {
        parsers.clear();
    }

}
