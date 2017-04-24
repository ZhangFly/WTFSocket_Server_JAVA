package wtf.socket.schedule;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang.StringUtils;
import wtf.socket.security.WTFSocketSecurity;
import wtf.socket.util.WTFSocketLogUtils;
import wtf.socket.exception.*;
import wtf.socket.inter.listener.WTFSocketDisconnectListener;
import wtf.socket.io.netty.WTFSocketTCPInitializer;
import wtf.socket.io.netty.WTFSocketWebSocketInitializer;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.protocol.WTFSocketProtocolFamily;
import wtf.socket.protocol.WTFSocketConnectType;
import wtf.socket.routing.WTFSocketCleaner;
import wtf.socket.routing.WTFSocketRoutingMap;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度器
 */
public class WTFSocketScheduler {

    /**
     * 服务器配置
     */
    private static WTFSocketConfig config = null;

    /**
     * 消息处理接口
     */
    private static WTFSocketHandler handler = (request, response) -> {
        // nothing to do
    };

    /**
     * 连接断开监听
     */
    private static WTFSocketDisconnectListener disconnectListener = item -> {
        // do nothing
    };

    /**
     * 提交一个数据包
     *
     * @param packet 数据包
     * @param ioTag 提交数据包的io的tag
     * @param connectType 提交数据的io的连接类型
     * @throws WTFSocketFatalException 致命异常
     */
    public static void submit(String packet, String ioTag, WTFSocketConnectType connectType) throws WTFSocketFatalException{
        try {
            final WTFSocketMsg msg = WTFSocketProtocolFamily.parseMsgFromString(packet);
            msg.setConnectType(connectType);
            msg.setIoTag(ioTag);

            // 数据源的地址不能为 server
            // server 为服务器保留地址
            if (StringUtils.equals(msg.getFrom(), "server"))
                throw new WTFSocketInvalidSourceException(msg.getFrom());

            WTFSocketLogUtils.receive(packet, msg);

            final List<WTFSocketMsg> responses = new ArrayList<>();
            handler.invoke(msg, responses);

            if (responses.isEmpty()) {
                sendMsg(msg);
            }else {
                sendMsg(responses);
            }

        } catch (WTFSocketCommonException e) {
            final WTFSocketMsg errResponse = e.getOriginalMsg().makeResponse();
            errResponse.setFrom("server");
            errResponse.setState(e.getErrCode());
            errResponse.setBody(new JSONObject() {{
                put("cause", e.getMessage());
            }});

            final String data = WTFSocketProtocolFamily.packageMsgToString(errResponse);
            WTFSocketLogUtils.exception(data, errResponse);
            if (WTFSocketRoutingMap.FORMAL.contains(errResponse.getTo())) {
                WTFSocketRoutingMap.FORMAL.getItem(errResponse.getTo()).getTerm().write(data);
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息对象
     * @throws WTFSocketInvalidSourceException 无效的消息源
     * @throws WTFSocketInvalidTargetException 无效的消息目标
     * @throws WTFSocketUnsupportedProtocolException 不被支持的协议
     * @throws WTFSocketPermissionDeniedException 无发送权限
     */
    public static void sendMsg(WTFSocketMsg msg) throws WTFSocketInvalidSourceException, WTFSocketInvalidTargetException, WTFSocketUnsupportedProtocolException, WTFSocketPermissionDeniedException {

        WTFSocketRoutingItem target;

        // 目标为DEBUG对象
        if (config.isUseDebug() && WTFSocketRoutingMap.DEBUG.contains(msg.getTo())) {
            target = WTFSocketRoutingMap.DEBUG.getItem(msg.getTo());
        }else {
            WTFSocketSecurity.check(msg);
            target = WTFSocketRoutingMap.FORMAL.getItem(msg.getTo());
        }

        msg.setVersion(target.getAccept());
        final String data = WTFSocketProtocolFamily.packageMsgToString(msg);

        if (config.isUseDebug()) {
            WTFSocketLogUtils.dispatch(data, msg);
        }
        target.getTerm().write(data);
    }

    /**
     * 发送一组消息
     *
     * @param msgs 消息对象数组
     * @throws WTFSocketInvalidSourceException 无效的消息源
     * @throws WTFSocketInvalidTargetException 无效的消息目标
     * @throws WTFSocketUnsupportedProtocolException 不被支持的协议
     * @throws WTFSocketPermissionDeniedException 无发送权限
     */
    public static void sendMsg(List<WTFSocketMsg> msgs) throws WTFSocketInvalidSourceException, WTFSocketInvalidTargetException, WTFSocketUnsupportedProtocolException, WTFSocketPermissionDeniedException {
        for (WTFSocketMsg protocol : msgs) {
            sendMsg(protocol);
        }
    }

    /**
     * 添加处理器
     *
     * @param handler 处理器
     */
    public static void setHandler(WTFSocketHandler handler) {
        if (handler != null) {
            WTFSocketScheduler.handler = handler;
        }
    }

    /**
     * 获取处理器
     *
     * @return 处理器
     */
    public static WTFSocketHandler getHandler() {
        return handler;
    }

    /**
     * 移除处理器
     */
    public static void removeHandler() {
        handler = (request, response) -> {
            // nothing to do
        };
    }

    /**
     * 设置断开连接监听器
     *
     * @return 监听器
     */
    public static WTFSocketDisconnectListener getDisconnectListener() {
        return disconnectListener;
    }

    /**
     * 设置断开连接监听器
     *
     * @param disconnectListener 监听器
     */
    public static void setDisconnectListener(WTFSocketDisconnectListener disconnectListener) {
        WTFSocketScheduler.disconnectListener = disconnectListener;
    }

    public static void removeDisconnectListener() {
        disconnectListener = item -> {
            // do nothing
        };
    }

    /**
     * 启动框架
     *
     * @param config 启动配置
     */
    public static void run(WTFSocketConfig config) {

        if (config == null) {
            return;
        }

        WTFSocketScheduler.config = config;

        if (config.getTcpPort() > 0) {
            startTcpServer(config.getTcpPort());
        }

        if (config.getWebSocketPort() > 0) {
            startWebSocketServer(config.getWebSocketPort());
        }

        WTFSocketCleaner.runExpire();
    }

    /**
     * 获取框架配置
     *
     * @return 框架配置
     */
    public static WTFSocketConfig getConfig() {
        return config;
    }

    // 开启WebSocket服务器
    private static void startWebSocketServer(int port) {
        startServer(
                port,
                new WTFSocketWebSocketInitializer()
        );
    }

    // 开启TCP服务器
    private static void startTcpServer(int port) {
        startServer(
                port,
                new WTFSocketTCPInitializer()
        );
    }

    // 开启服务器
    private static void startServer(final int port, final ChannelInitializer initializer) {

        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(initializer)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture f = b.bind(port).sync();

                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();

    }
}
