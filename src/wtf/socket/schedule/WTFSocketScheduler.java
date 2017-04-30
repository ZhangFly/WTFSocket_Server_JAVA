package wtf.socket.schedule;

import com.alibaba.fastjson.JSONObject;
import wtf.socket.WTFSocketServer;
import wtf.socket.controller.WTFSocketControllers;
import wtf.socket.controller.WTFSocketControllersGroup;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketFatalException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.exception.fatal.WTFSocketProtocolUnsupportedException;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.exception.normal.WTFSocketNormalException;
import wtf.socket.exception.normal.WTFSocketPermissionDeniedException;
import wtf.socket.io.WTFSocketIOBooter;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;
import wtf.socket.secure.WTFSocketSecureCheck;
import wtf.socket.util.WTFSocketLogUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 调度器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public class WTFSocketScheduler {

    private final WTFSocketServer context;

    public WTFSocketScheduler(WTFSocketServer context) {
        this.context = context;
        WTFSocketServer.SPRING.getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * 消息处理接口
     * 默认使用WTFSocketControllersGroup
     */
    @Resource(name = "wtf.socket.controllersGroup")
    private WTFSocketHandler handler;

    /**
     * 接收到消息时进行的安全检查
     */
    @Resource(name = "wtf.socket.secure.onReceive")
    private WTFSocketSecureCheck onReceiveSecure;

    /**
     * 发送消息前进行的安全检查
     */
    @Resource(name = "wtf.socket.secure.beforeSend")
    private WTFSocketSecureCheck beforeSendSecure;

    /**
     * io层启动器
     * 默认使用NettyBooter
     */
    @Resource(name = "wtf.socket.nettyBooter")
    private WTFSocketIOBooter ioBooter;

    /**
     * 向服务器提交一个数据包
     * 一般是有io层发起
     *
     * @param packet      数据包
     * @param ioTag       提交数据包的io的标记
     * @param connectType 提交数据的io的连接类型
     *
     * @throws WTFSocketFatalException 致命异常
     */
    public void submit(String packet, String ioTag, String connectType) throws WTFSocketException {
        try {
            // 解析数据
            final WTFSocketMsg msg = context.getProtocolFamily().parseMsgFromString(packet);
            msg.setConnectType(connectType);
            msg.setIoTag(ioTag);

            // 查找发送源
            final WTFSocketRoutingItem item = context.getRouting().getItem(ioTag);
            context.getEventsGroup().eventOccurred(item, msg, WTFSocketEventsType.OnReceiveData);

            onReceiveSecure.check(context, msg);

            if (context.getConfig().isUseDebug())
                WTFSocketLogUtils.received(context, packet, msg);

            final List<WTFSocketMsg> responses = new ArrayList<>();
            if (handler != null)
                handler.handle(item, msg, responses);

            sendMsg(responses);
        } catch (WTFSocketNormalException e) {
            if (e.getOriginalMsg() != null) {
                final WTFSocketMsg errResponse = e.getOriginalMsg().makeResponse();
                errResponse.setFrom("server");
                errResponse.setState(e.getErrCode());
                errResponse.setBody(new JSONObject() {{
                    put("cause", e.getMessage());
                }});

                final String data = context.getProtocolFamily().packageMsgToString(errResponse);
                if (context.getRouting().getFormalMap().contains(errResponse.getTo())) {
                    context.getRouting().getFormalMap().getItem(errResponse.getTo()).getTerm().write(data + context.getConfig().getFirstEOT());
                }

                if (context.getConfig().isUseDebug())
                    WTFSocketLogUtils.exception(context, data, errResponse);
            } else {
                context.getRouting().getItem(ioTag).getTerm().write(e.getMessage() + context.getConfig().getFirstEOT());
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息对象
     *
     * @throws WTFSocketInvalidSourceException       无效的消息源
     * @throws WTFSocketInvalidTargetException       无效的消息目标
     * @throws WTFSocketProtocolUnsupportedException 不被支持的协议
     * @throws WTFSocketPermissionDeniedException    无发送权限
     */
    public void sendMsg(WTFSocketMsg msg) throws WTFSocketException {
        WTFSocketRoutingItem target;

        if (context.getConfig().isUseDebug() && context.getRouting().getDebugMap().contains(msg.getTo())) {
            target = context.getRouting().getDebugMap().getItem(msg.getTo());
        } else {
            beforeSendSecure.check(context, msg);
            target = context.getRouting().getFormalMap().getItem(msg.getTo());
        }
        msg.setVersion(target.getAccept());
        final String data = context.getProtocolFamily().packageMsgToString(msg);
        context.getEventsGroup().eventOccurred(target, msg, WTFSocketEventsType.BeforeSendData);

        if (context.getConfig().isUseDebug())
            WTFSocketLogUtils.forwarded(context, data, msg);

        target.getTerm().write(data + context.getConfig().getFirstEOT());
    }

    /**
     * 发送一组消息
     *
     * @param msgs 消息对象数组
     *
     * @throws WTFSocketInvalidSourceException       无效的消息源
     * @throws WTFSocketInvalidTargetException       无效的消息目标
     * @throws WTFSocketProtocolUnsupportedException 不被支持的协议
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
     * 启动框架调度器
     */
    public void run() {
        assert context.getConfig() != null;

        new WTFSocketRoutingTmpItem(context, new WTFSocketDefaultIOTerm()) {{
            setAddress("server");
            setCover(false);
            shiftToFormal();
        }};
        new WTFSocketRoutingTmpItem(context, new WTFSocketDefaultIOTerm()) {{
            setAddress("heartbeat");
            setCover(false);
            shiftToFormal();
        }};

        // 如果可能，使用spring扫描加载控制器
        if (WTFSocketServer.SPRING.getResource("spring.xml").exists() && handler instanceof WTFSocketControllersGroup)
            ((WTFSocketControllersGroup) handler).addControllerFromSpringBeans();

        // 如果需要，加载消息转发控制器
        if (context.getConfig().isUseMsgForward() && handler instanceof WTFSocketControllersGroup)
            ((WTFSocketControllersGroup) handler).addController(WTFSocketControllers.msgForwardingController());

        // 启动io层
        ioBooter.work(new HashMap<String, Object>() {{
            put("tcpPort", context.getConfig().getTcpPort());
            put("webSocketPort", context.getConfig().getWebSocketPort());
            put("keepAlive", context.getConfig().isKeepAlive());
            put("context", context);
        }});

        // 如果需要开启临时用户清理任务
        if (context.getConfig().isCleanEmptyConnect()) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    () -> context.getRouting().getTmpMap().values().stream()
                            .filter(WTFSocketRoutingTmpItem::isExpires)
                            .forEach(item -> {
                                item.getTerm().close();
                                context.getRouting().getTmpMap().remove(item);
                            }),
                    1, 1, TimeUnit.MINUTES);
        }
    }

}
