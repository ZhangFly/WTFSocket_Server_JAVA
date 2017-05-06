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
import wtf.socket.event.WTFSocketEventListener;
import wtf.socket.io.WTFSocketIOLauncher;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Netty 启动器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public class WTFSocketNettyLauncherImpl implements WTFSocketIOLauncher {

    private static final Log logger = LogFactory.getLog(WTFSocketNettyLauncherImpl.class);

    WTFSocketNettyLauncherImpl() {}

    @Resource
    private WTFSocketTCPInitializer tcpInitializer;
    @Resource
    private WTFSocketWebSocketInitializer webSocketInitializer;

    public void work(Map<String, Object> config) {
        final Integer tcpPort = (Integer) config.get("tcpPort");
        final Integer webSocketPort = (Integer) config.get("webSocketPort");
        final Boolean keepAlive = (Boolean) config.get("keepAlive");

        if (tcpPort != null && tcpPort > 0)
            startTcpServer(tcpPort, keepAlive, (item, info) -> logger.info("Listening Port[TCP] on [" + tcpPort + "]..."));
        if (webSocketPort != null && webSocketPort > 0)
            startWebSocketServer(webSocketPort, keepAlive, (item, info) -> logger.info("Listening Port[WebSocket] on [" + webSocketPort + "]..."));
    }

    // 开启WebSocket服务器
    private void startWebSocketServer(int port, boolean keepAlive, WTFSocketEventListener startedListener) {
        startServer(
                port,
                keepAlive,
                webSocketInitializer,
                startedListener
        );
    }

    // 开启TCP服务器
    private void startTcpServer(int port, boolean keepAlive, WTFSocketEventListener startedListener) {
        startServer(
                port,
                keepAlive,
                tcpInitializer,
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
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                        logger.info(initializer.getClass().getSimpleName() + "shutdown");
                    }));
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
