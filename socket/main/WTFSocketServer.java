package wtf.socket.main;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import wtf.socket.exception.*;
import wtf.socket.netty.handlers.WTFSocketTCPInitializer;
import wtf.socket.netty.handlers.WTFSocketWebSocketInitializer;
import wtf.socket.protocols.parser.WTFSocketProtocolParser;
import wtf.socket.protocols.templates.WTFSocketConnectType;
import wtf.socket.protocols.templates.WTFSocketProtocol;
import wtf.socket.protocols.templates.WTFSocketProtocol_2_0;
import wtf.socket.registry.WTFSocketRegistry;
import wtf.socket.registry.items.WTFSocketRegistryDebugItem;
import wtf.socket.registry.items.WTFSocketRegistryItem;
import wtf.socket.registry.items.WTFSocketRegistryUserItem;
import wtf.socket.registry.items.WTFSocketUserType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 服务器框架
 */
public class WTFSocketServer {

    private static Logger logger = Logger.getLogger("wtf.server");

    // 配置
    private static WTFSocketConfig config = null;

    // 传输终止符号
    private static final String EOT = "\r\n";

    // 默认服务函数
    // 使用静态变量减少反复创建对象的性能消耗
    private static final WTFSocketHandler DEFAULT_HANDLER = new WTFSocketHandler() {
        @Override
        public void invoke(Channel ctx, WTFSocketProtocol request, List<WTFSocketProtocol> responses) {
            // nothing to do
        }
    };

    // 消息处理接口
    private static WTFSocketHandler handler = DEFAULT_HANDLER;

    /**
     * 提交一个数据到框架
     *
     * @param ctx    收到数据的连接
     * @param packet 数据包
     */
    public static void submit(ChannelHandlerContext ctx, String packet, WTFSocketConnectType connectType) {

        try {
            WTFSocketProtocol protocol = WTFSocketProtocolParser.parse(packet, connectType);

            // 回复心跳包
            if (protocol.getMsgType() == 0) {

                debugOutput(String.format(
                        "receive<%s> from <%s> to <%s>:\n%s\n",
                        protocol.getConnectType(),
                        protocol.getFrom(),
                        protocol.getTo(),
                        packet), protocol.getFrom(), true);

                replyHeartbeat(protocol);
                return;
            }

            debugOutput(String.format(
                    "receive<%s> from <%s> to <%s>:\n%s\n",
                    protocol.getConnectType(),
                    protocol.getFrom(),
                    protocol.getTo(),
                    packet), protocol.getFrom(), false);

            List<WTFSocketProtocol> responses = new ArrayList<>();
            // 触发服务函数
            handler.invoke(ctx.channel(), protocol, responses);

            if (responses.isEmpty()) {
                // 服务函数未生成回复数据
                // 转发原数据
                sendMsg(protocol);
            }else {
                // 服务函数生成回复数据
                // 转发生成的数据
                sendMsg(responses);
            }

            return;
        } catch (WTFSocketException e) {

            WTFSocketProtocol errResponse = null;

            if (e instanceof WTFSocketCommonException) {
                errResponse = WTFSocketProtocol_2_0.makeResponse(((WTFSocketCommonException) e).getOriginal());
            }
            if (e instanceof WTFSocketFatalException) {
                errResponse = new WTFSocketProtocol_2_0();
                errResponse.setFrom("server");
            }

            errResponse.setState(e.getErrCode());
            JSONObject body = new JSONObject();
            body.put("cause", e.getMessage());
            errResponse.setBody(body);

            // 转换到发送方接收的协议格式
            // sendMsg方法是从注册表中查询对方接收协议格式的
            // 发送异常时对方可能尚未完成注册
            // 所以这里不适合调用sendMsg方法
            String data = null;
            try {
                data = WTFSocketProtocolParser.convertToString(errResponse, errResponse.getVersion());
//                data = protocol == null ?
//                        WTFSocketProtocolParser.convertToString(errResponse, errResponse.getVersion()) :
//                        WTFSocketProtocolParser.convertToString(errResponse, protocol.getVersion());
            } catch (WTFSocketInvalidProtocolVersionException e1) {
                e1.printStackTrace();
            }

            debugOutput(String.format(
                    "exception<%s> from <%s>:\n%s\n",
                    errResponse.getConnectType(),
                    errResponse.getTo(),
                    data), errResponse.getTo(), false);

            writeAndFlush(ctx.channel(), data, errResponse.getConnectType());
        }

    }

    /**
     * 发送数据
     *
     * @param protocol    协议包
     * @throws WTFSocketInvalidTargetException          无效通信目标
     * @throws WTFSocketInvalidProtocolVersionException 无效协议版本
     */
    public static void sendMsg(WTFSocketProtocol protocol) throws WTFSocketInvalidTargetException, WTFSocketInvalidProtocolVersionException {


        if (!WTFSocketRegistry.contains(protocol.getTo())) {
            WTFSocketInvalidTargetException exception = new WTFSocketInvalidTargetException(protocol.getTo());
            exception.setOriginal(protocol);
            throw exception;
        }

        WTFSocketRegistryUserItem user = (WTFSocketRegistryUserItem) WTFSocketRegistry.get(protocol.getTo());
        String data = WTFSocketProtocolParser.convertToString(protocol, user.getAccept());

        debugOutput(String.format(
                "dispatch<%s> from <%s> to <%s>:\n%s\n",
                user.getConnectType(),
                protocol.getFrom(),
                protocol.getTo(),
                data), protocol.getTo(), false);

        writeAndFlush(user.getChannel(), data, user.getConnectType());
    }

    /**
     * 群发消息
     *
     * @param protocols 消息列表
     */
    public static void sendMsg(List<WTFSocketProtocol> protocols) throws WTFSocketInvalidTargetException, WTFSocketInvalidProtocolVersionException {
        for (WTFSocketProtocol protocol : protocols) {
            sendMsg(protocol);
        }
    }

    /**
     * 添加处理者
     *
     * @param handler 处理者
     */
    public static void setHandler(WTFSocketHandler handler) {
        if (handler != null) {
            WTFSocketServer.handler = handler;
        }
    }

    /**
     * 移除处理者
     */
    public static void removeHandler() {
        handler = DEFAULT_HANDLER;
    }

    /**
     * 启动框架
     *
     * @param config 启动配置
     */
    public static void run(WTFSocketConfig config) {

        if (config == null) {
            logger.log(Level.WARNING, "config can not be null");
            return;
        }

        WTFSocketServer.config = config;

        if (config.getTcpPort() > 0) {
            startTcpServer(config.getTcpPort());
        }

        if (config.getWebSocketPort() > 0) {
            startWebSocketServer(config.getWebSocketPort());
        }

        if (config.isCheckHeartbeat()) {
           WTFSocketRegistry.runExpire();
        }
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

        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }).start();

    }

    // 回复心跳包
    private static void replyHeartbeat(WTFSocketProtocol protocol) throws WTFSocketInvalidTargetException, WTFSocketInvalidProtocolVersionException {

        String from = protocol.getFrom();
        String to = protocol.getTo();
        protocol.setFrom(to);
        protocol.setTo(from);

        WTFSocketRegistry.feed(from);

        if (!WTFSocketRegistry.contains(protocol.getTo())) {
            throw new WTFSocketInvalidTargetException(protocol.getTo());
        }

        WTFSocketRegistryUserItem user = (WTFSocketRegistryUserItem) WTFSocketRegistry.get(protocol.getTo());
        String data = WTFSocketProtocolParser.convertToString(protocol, user.getAccept());

        debugOutput(String.format(
                "reply heartbeat<%s> from <%s>:\n%s",
                protocol.getConnectType(),
                protocol.getTo(),
                data
        ),protocol.getTo(),true);

        writeAndFlush(user.getChannel(), data, protocol.getConnectType());
    }

    // 执行写操作
    private static void writeAndFlush(Channel ctx, String data, WTFSocketConnectType connectType) {

        if (ctx == null || !ctx.isOpen()) {
            return;
        }

        switch (connectType) {
            case TCP:
                ByteBuf byteBuf = Unpooled.copiedBuffer((data + EOT).getBytes());
                ctx.writeAndFlush(byteBuf);
                break;
            case WebSocket:
                ctx.writeAndFlush(new TextWebSocketFrame((data + EOT)));
                break;
            default:
                break;
        }
    }

    // 输出调试信息
    private static void debugOutput(String msg, String to, boolean isHeartbeat) {

        logger.info(msg);

        if (!config.isUseDebug()) {
            return;
        }

        if (!WTFSocketRegistry.isDebug(to)) {
            for (WTFSocketRegistryItem item : WTFSocketRegistry.values(WTFSocketUserType.DEBUG)) {

                WTFSocketRegistryDebugItem debug = (WTFSocketRegistryDebugItem)item;

                if (isHeartbeat && !debug.isShowHeartbeatMsg()) {
                    continue;
                }

                if (debug.getChannel() != null) {
                    if (debug.isFilter(msg)) {
                        writeAndFlush(item.getChannel(), msg, item.getConnectType());
                    }
                }
            }
        }
    }

}
