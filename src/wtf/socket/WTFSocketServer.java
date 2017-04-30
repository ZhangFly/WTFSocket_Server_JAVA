package wtf.socket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.controller.WTFSocketControllersGroup;
import wtf.socket.event.WTFSocketEventsGroup;
import wtf.socket.protocol.WTFSocketProtocolFamily;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.schedule.WTFSocketScheduler;
import wtf.socket.secure.WTFSocketSecure;

/**
 * WTFSocket服务器
 * <p>
 * Created by ZFly on 2017/4/25.
 */

public class WTFSocketServer {

    /**
     * Spring 上下问
     */
    public static final ApplicationContext SPRING = new ClassPathXmlApplicationContext("spring.wtf.socket.xml");

    /**
     * 消息调度组件
     * 根据消息的头信息将消息投递到指定的目的地
     */
    private final WTFSocketScheduler scheduler = new WTFSocketScheduler(this);

    /**
     * 协议族组件
     * IO层收到数据后选择合适的解析器将数据解析为标准消息格式
     */
    private final WTFSocketProtocolFamily protocolFamily = new WTFSocketProtocolFamily();

    /**
     * 安全组件
     * 可用添加一些安全策略，如发送数据的授权许可等
     */
    private final WTFSocketSecure secure = new WTFSocketSecure();

    /**
     * 路由组件
     * 查询和记录连接的地址
     */
    private final WTFSocketRouting routing = new WTFSocketRouting(this);

    /**
     * 事件组组件
     * 包含了一些服务器的监听事件
     */
    private final WTFSocketEventsGroup eventsGroup = new WTFSocketEventsGroup();

    /**
     * 服务器配置
     */
    private WTFSocketConfig config;

    public void run(WTFSocketConfig config) {
        this.config = config;
        scheduler.run();
    }

    public WTFSocketServer addController(WTFSocketController controller) {
        if (scheduler.getHandler() instanceof WTFSocketControllersGroup) {
            ((WTFSocketControllersGroup) scheduler.getHandler()).addController(controller);
        }
        return this;
    }

    public WTFSocketScheduler getScheduler() {
        return scheduler;
    }

    public WTFSocketProtocolFamily getProtocolFamily() {
        return protocolFamily;
    }

    public WTFSocketSecure getSecure() {
        return secure;
    }

    public WTFSocketRouting getRouting() {
        return routing;
    }

    public WTFSocketEventsGroup getEventsGroup() {
        return eventsGroup;
    }

    public WTFSocketConfig getConfig() {
        return config;
    }

}
