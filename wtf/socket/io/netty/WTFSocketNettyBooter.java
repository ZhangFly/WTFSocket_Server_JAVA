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
import wtf.socket.io.WTFSocketIOBooter;
import wtf.socket.schedule.WTFSocketScheduler;

import java.util.Map;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.nettyBooter")
public class WTFSocketNettyBooter implements WTFSocketIOBooter {

    private static final Log logger = LogFactory.getLog(WTFSocketNettyBooter.class);

    public void work(Map<String, Object> config) {
        int tcpPort = 0;
        int webSocketPort = 0;
        boolean keepAlive = true;

        if (config.containsKey("tcpPort")) tcpPort = (int) config.get("tcpPort");
        if (config.containsKey("webSocketPort")) webSocketPort = (int) config.get("webSocketPort");
        if (config.containsKey("keepAlive")) keepAlive = (boolean) config.get("keepAlive");

        if (tcpPort > 0) startTcpServer(tcpPort, keepAlive);
        if (webSocketPort > 0) startWebSocketServer(webSocketPort, keepAlive);
    }

    // 开启WebSocket服务器
    private void startWebSocketServer(int port, boolean keepAlive) {
        logger.info("Listening port<WebSocket> on [" + port + "] ...");
        startServer(
                port,
                keepAlive,
                new WTFSocketWebSocketInitializer()
        );
    }

    // 开启TCP服务器
    private void startTcpServer(int port, boolean keepAlive) {
        logger.info("Listening port<TCP> on [" + port + "] ...");
        startServer(
                port,
                keepAlive,
                new WTFSocketTCPInitializer()
        );
    }

    // 开启服务器
    private void startServer(final int port, boolean keepAlive, final ChannelInitializer initializer) {

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
