package wtf.socket.protocols.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import wtf.socket.exception.WTFSocketInvalidProtocolVersionException;
import wtf.socket.exception.WTFSocketLackNecessaryAttrsException;
import wtf.socket.exception.WTFSocketProtocolFormatWrongException;
import wtf.socket.protocols.templates.WTFSocketConnectType;
import wtf.socket.protocols.templates.WTFSocketProtocol;
import wtf.socket.protocols.templates.WTFSocketProtocol_2_0;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 协议解释器
 */
public class WTFSocketProtocolParser {

    /**
     * 已经装载的协议
     */
    private static ConcurrentHashMap<String, WTFSocketProtocolParserItem> protocols = new ConcurrentHashMap<>();

    /**
     * 向解释器中添加协议
     * 因为动态删除协议会导致一些列错误，所有不需要动态的删除协议
     *
     * @param pClass 协议
     */
    public static void addProtocol(Class<? extends WTFSocketProtocol> pClass) {

        final WTFSocketProtocolParserItem item = new WTFSocketProtocolParserItem(pClass);

        protocols.put(item.getVersion(), item);

    }


    /**
     * 从原始数据中解析出一个默认协议对象
     *
     * @param packet 原始数据
     * @return  协议对象
     */
    public static WTFSocketProtocol parse(String packet, WTFSocketConnectType connectType) throws WTFSocketProtocolFormatWrongException,WTFSocketInvalidProtocolVersionException,WTFSocketLackNecessaryAttrsException {

        JSONObject json;

        try {
            json = JSON.parseObject(packet);
        }catch (Exception e) {
            throw new WTFSocketProtocolFormatWrongException(e.getMessage() + " actual => " + packet);
        }

        String version = json.containsKey("version") ?
                json.getString("version") :
                "1.0";

        if (!protocols.containsKey(version)) {
            throw new WTFSocketInvalidProtocolVersionException(version);
        }

        WTFSocketProtocolParserItem item =  protocols.get(version);

        for (String attr : item.getNecessaryAttrs()) {
            if (!json.containsKey(attr)) {
                throw new WTFSocketLackNecessaryAttrsException(attr);
            }
        }

        WTFSocketProtocol protocol;

        try {
            protocol = json.toJavaObject(item.getpClass());
        }catch (Exception e) {
            throw new WTFSocketProtocolFormatWrongException(e.getMessage());
        }

        protocol.setConnectType(connectType);

        return protocol;
    }

    /**
     * 将默认协议转换到指定协议
     *
     * @param protocol 默认协议
     * @param version 指定协议版本
     * @return 协议对象
     */
    public static WTFSocketProtocol convert(WTFSocketProtocol protocol, String version) throws WTFSocketInvalidProtocolVersionException {

        if (!protocols.containsKey(version)) {
            throw new WTFSocketInvalidProtocolVersionException(version);
        }

        WTFSocketProtocolParserItem item = protocols.get(version);

        if (StringUtils.equals(protocol.getVersion(), item.getVersion())) {
            return protocol;
        }else {
            try {
                Constructor constructor = protocols.get(version).getpClass().getDeclaredConstructor(WTFSocketProtocol.class);
                return (WTFSocketProtocol) constructor.newInstance(protocol);
            }catch (Exception e) {
                e.printStackTrace();
                return new WTFSocketProtocol_2_0();
            }
        }
    }

    /**
     * 将默认协议转换到指定协议，并返回其JSONString
     *
     * @param protocol 默认协议
     * @param version 指定协议版本
     * @return 协议对象JSONString
     */
    public static String convertToString(WTFSocketProtocol protocol, String version) throws WTFSocketInvalidProtocolVersionException {

        return JSON.toJSONString(convert(protocol, version));

    }
}
