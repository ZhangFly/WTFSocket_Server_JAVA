package wtf.socket.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocketServer;
import wtf.socket.controller.WTFSocketControllerGroup;
import wtf.socket.controller.WTFSocketControllers;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOLauncher;
import wtf.socket.routing.client.WTFSocketSystemClient;
import wtf.socket.routing.client.WTFSocketTmpClient;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.request.WTFSocketRichRequestImpl;
import wtf.socket.workflow.response.WTFSocketRichResponse;
import wtf.socket.workflow.response.WTFSocketRichResponseImpl;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 调度器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public class WTFSocketWorkflow {

    private Log logger = LogFactory.getLog(WTFSocketWorkflow.class);

    private WTFSocketServer context;

    private WTFSocketIOLauncher ioLauncher;

    private WTFSocketWork workQueue[];

    private WTFSocketWork basicWork;

    /**
     * 由spring负责创建
     *
     * @param context 服务器上下文
     * @param ioLauncher io层启动器
     * @param workQueue 工作队列
     * @param basicWork 基础工作，在工作队列完成后调用
     */
    private WTFSocketWorkflow(WTFSocketServer context, WTFSocketIOLauncher ioLauncher, WTFSocketWork[] workQueue, WTFSocketWork basicWork) {
        this.context = context;
        this.ioLauncher = ioLauncher;
        this.workQueue = workQueue;
        this.basicWork = basicWork;
    }

    /**
     * 向服务器提交一个数据包
     * 一般是有io层发起
     *
     * @param packet      数据包
     * @param ioTag       提交数据包的io的标记
     * @param connectType 提交数据的io的连接类型
     */
    public void submit(String packet, String ioTag, String connectType) {

        final WTFSocketRichRequest request = context.getSpring().getBean(WTFSocketRichRequestImpl.class);
        final WTFSocketRichResponse response = context.getSpring().getBean(WTFSocketRichResponseImpl.class);

        request.setDataPacket(packet);
        request.setIOTag(ioTag);
        request.setConnectType(connectType);

        logger.debug("Receive dataPacket:\n" + packet);

        for (WTFSocketWork work : workQueue) {
            if (null != request.getException()) {
                break;
            }
            work.execute(request, response);
        }

        basicWork.execute(request, response);
    }

    /**
     * 启动框架调度器
     */
    public void run() {

        assert context.getConfig() != null;

        logger.info("Run server with config:\n" + context.getConfig());

        try {
            // 在正式表中创建两个通讯对象
            final WTFSocketSystemClient serverItem = context.getSpring().getBean(WTFSocketSystemClient.class);
            serverItem.setAddress("server");
            context.getRouting().getFormalMap().add(serverItem);

            final WTFSocketSystemClient heartbeatItem = context.getSpring().getBean(WTFSocketSystemClient.class);
            heartbeatItem.setAddress("heartbeat");
            context.getRouting().getFormalMap().add(heartbeatItem);
        } catch (WTFSocketException e) {
            logger.error("", e);
        }

        final WTFSocketControllerGroup controllerGroup = context.getSpring().getBean(WTFSocketControllerGroup.class);

        // 如果可能，使用spring扫描加载控制器
        if (context.getSpring().getResource("spring.xml").exists())
            controllerGroup.addControllerFromSpringBeans();

        // 如果需要，加载消息转发控制器
        if (context.getConfig().isMsgForward())
            controllerGroup.add(WTFSocketControllers.msgForwardingController());

        // 启动io层
        ioLauncher.work(new HashMap<String, Object>() {{
            put("tcpPort", context.getConfig().getTcpPort());
            put("webSocketPort", context.getConfig().getWebSocketPort());
            put("keepAlive", context.getConfig().isKeepAlive());
        }});

        // 如果需要开启临时用户清理任务
        if (context.getConfig().isCleanEmptyConnect()) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    () -> context.getRouting().getTmpMap().values().stream()
                            .filter(WTFSocketTmpClient::isExpires)
                            .forEach(item -> {
                                item.getTerm().close();
                                context.getRouting().getTmpMap().remove(item);
                            }),
                    1, 1, TimeUnit.MINUTES);
        }
    }
}
