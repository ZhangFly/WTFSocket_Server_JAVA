package wtf.socket.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.event.WTFSocketEventListener;
import wtf.socket.io.WTFSocketIOBooter;

import java.util.Map;

/**
 * Netty 启动器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.nettyBooter")
public class WTFSocketNettyBooterImpl implements WTFSocketIOBooter {

    private static final Log logger = LogFactory.getLog(WTFSocketNettyBooterImpl.class);

    public void work(Map<String, Object> config) {
        final WTFSocketServer context = (WTFSocketServer) config.get("context");
        final Integer tcpPort = (Integer) config.get("tcpPort");
        final Integer webSocketPort = (Integer) config.get("webSocketPort");
        final Boolean keepAlive = (Boolean) config.get("keepAlive");

        if (tcpPort != null && tcpPort > 0)
            startTcpServer(context, tcpPort, keepAlive, (item, info) -> logger.info("Listening Port[TCP] on [" + tcpPort + "]..."));
        if (webSocketPort != null && webSocketPort > 0)
            startWebSocketServer(context, webSocketPort, keepAlive, (item, info) -> logger.info("Listening Port[WebSocket] on [" + webSocketPort + "]..."));
    }

    // 开启WebSocket服务器
    private void startWebSocketServer(WTFSocketServer context, int port, boolean keepAlive, WTFSocketEventListener startedListener) {
        startServer(
                port,
                keepAlive,
                new WTFSocketWebSocketInitializer(context),
                startedListener
        );
    }

    // 开启TCP服务器
    private void startTcpServer(WTFSocketServer context, int port, boolean keepAlive, WTFSocketEventListener startedListener) {
        startServer(
                port,
                keepAlive,
                new WTFSocketTCPInitializer(context),
                startedListener
        );
    }

    // 开启服务器
    private void startServer(final int port, boolean keepAlive, final ChannelInitializer initializer, WTFSocketEventListener startedListener) {

        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(initializer)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, keepAlive);

                ChannelFuture f = b.bind(port).addListener(future -> {
                    if (startedListener != null) startedListener.eventOccurred(null, null);
                }).sync();

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
