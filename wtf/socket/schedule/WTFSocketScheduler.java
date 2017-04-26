package wtf.socket.schedule;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import wtf.socket.controller.WTFSocketControllerGroup;
import wtf.socket.WTFSocket;
import wtf.socket.exception.normal.WTFSocketNormalException;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.exception.normal.WTFSocketPermissionDeniedException;
import wtf.socket.exception.fatal.WTFSocketFatalException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.exception.fatal.WTFSocketUnsupportedProtocolException;
import wtf.socket.io.WTFSocketIOBooter;
import wtf.socket.secure.WTFSocketSecureCheck;
import wtf.socket.util.WTFSocketDebugUtils;
import wtf.socket.exception.*;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 调度器
 */
@Component("wtf.socket.scheduler")
public class WTFSocketScheduler {

    /**
     * 服务器配置
     */
    private WTFSocketConfig config = null;

    /**
     * 消息处理接口
     */
    @Resource(name = "wtf.socket.controllerGroup")
    private WTFSocketHandler handler;

    @Resource(name = "wtf.socket.secure.onReceive")
    private WTFSocketSecureCheck onReceiveSecure;

    @Resource(name = "wtf.socket.secure.beforeSend")
    private WTFSocketSecureCheck beforeSendSecure;

    @Resource(name = "wtf.socket.cleaner")
    private WTFSocketCleaner cleaner;

    @Resource(name = "wtf.socket.nettyBooter")
    private WTFSocketIOBooter ioBooter;

    /**
     * 提交一个数据包
     *
     * @param packet      数据包
     * @param ioTag       提交数据包的io的tag
     * @param connectType 提交数据的io的连接类型
     * @throws WTFSocketFatalException 致命异常
     */
    public void submit(String packet, String ioTag, String connectType) throws WTFSocketException {
        try {
            final WTFSocketMsg msg = WTFSocket.PROTOCOL_FAMILY.parseMsgFromString(packet);
            msg.setConnectType(connectType);
            msg.setIoTag(ioTag);

            onReceiveSecure.check(msg);

            if (config.isUseDebug())
                WTFSocketDebugUtils.receive(packet, msg);

            final List<WTFSocketMsg> responses = new ArrayList<>();
            if (handler != null)
                handler.handle(msg, responses);

            if (responses.isEmpty()) {
                sendMsg(msg);
            } else {
                sendMsg(responses);
            }
        } catch (WTFSocketNormalException e) {

            final WTFSocketMsg errResponse = e.getOriginalMsg().makeResponse();
            errResponse.setFrom("server");
            errResponse.setState(e.getErrCode());
            errResponse.setBody(new JSONObject() {{
                put("cause", e.getMessage());
            }});

            final String data = WTFSocket.PROTOCOL_FAMILY.packageMsgToString(errResponse);
            if (WTFSocket.ROUTING.FORMAL_MAP.contains(errResponse.getTo())) {
                WTFSocket.ROUTING.FORMAL_MAP.getItem(errResponse.getTo()).getTerm().write(data);
            }

            if (config.isUseDebug())
                WTFSocketDebugUtils.exception(data, errResponse);
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息对象
     * @throws WTFSocketInvalidSourceException       无效的消息源
     * @throws WTFSocketInvalidTargetException       无效的消息目标
     * @throws WTFSocketUnsupportedProtocolException 不被支持的协议
     * @throws WTFSocketPermissionDeniedException    无发送权限
     */
    public void sendMsg(WTFSocketMsg msg) throws WTFSocketException {
        WTFSocketRoutingItem target;

        if (config.isUseDebug() && WTFSocket.ROUTING.DEBUG_MAP.contains(msg.getTo())) {
            target = WTFSocket.ROUTING.DEBUG_MAP.getItem(msg.getTo());
        } else {
            beforeSendSecure.check(msg);
            target = WTFSocket.ROUTING.FORMAL_MAP.getItem(msg.getTo());
        }

        msg.setVersion(target.getAccept());
        final String data = WTFSocket.PROTOCOL_FAMILY.packageMsgToString(msg);

        if (config.isUseDebug())
            WTFSocketDebugUtils.send(data, msg);

        target.getTerm().write(data);
    }

    /**
     * 发送一组消息
     *
     * @param msgs 消息对象数组
     * @throws WTFSocketInvalidSourceException       无效的消息源
     * @throws WTFSocketInvalidTargetException       无效的消息目标
     * @throws WTFSocketUnsupportedProtocolException 不被支持的协议
     * @throws WTFSocketPermissionDeniedException    无发送权限
     */
    public void sendMsg(List<WTFSocketMsg> msgs) throws WTFSocketException {
        for (WTFSocketMsg protocol : msgs) {
            sendMsg(protocol);
        }
    }

    /**
     * 添加处理器
     *
     * @param handler 处理器
     */
    public void setHandler(WTFSocketHandler handler) {
        this.handler = handler;
    }

    /**
     * 获取处理器
     *
     * @return 处理器
     */
    public WTFSocketHandler getHandler() {
        return handler;
    }

    /**
     * 启动框架
     *
     * @param config 启动配置
     */
    public void run(WTFSocketConfig config) {
        assert config != null;

        this.config = config;
        if (config.isCleanEmptyConnect())
            cleaner.work();
        if (WTFSocket.CONTEXT.getResource("spring.xml").exists() && handler instanceof WTFSocketControllerGroup)
            ((WTFSocketControllerGroup) handler).loadSpringConfig();
        ioBooter.work(new HashMap<String, Object>() {{
            put("tcpPort", config.getTcpPort());
            put("webSocketPort", config.getWebSocketPort());
            put("keepAlive", config.isKeepAlive());
        }});
    }

    /**
     * 获取框架配置
     *
     * @return 框架配置
     */
    public WTFSocketConfig getConfig() {
        return config;
    }


}
