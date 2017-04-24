package wtf.socket.protocol;

import wtf.socket.exception.WTFSocketMsgFormatWrongException;

/**
 * 协议解析器接口
 *
 * Created by zfly on 2017/4/21.
 */
public interface WTFSocketParser {
    /**
     * 是否负责该字符串数据
     *
     * @param data 原始数据
     * @return 是否负责
     */
    boolean isResponse(String data);

    /**
     * 是否负责该消息对象
     *
     * @param msg 消息对象
     * @return 是否负责
     */
    boolean isResponse(WTFSocketMsg msg);

    /**
     * 从字符串数据中解析数据生产消息对象
     *
     * @param data 原始数据
     * @return 消息对象
     */
    WTFSocketMsg parseMsgFromString(String data) throws WTFSocketMsgFormatWrongException;

    /**
     * 将消息对象打包成字符串数据
     *
     * @param msg 消息对象
     * @return 打包后的字符串
     */
    String packageMsgToString(WTFSocketMsg msg);
}
