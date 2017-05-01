package wtf.socket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.controller.WTFSocketControllersGroup;
import wtf.socket.event.WTFSocketEventListenersGroup;
import wtf.socket.protocol.WTFSocketProtocolFamily;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.schedule.WTFSocketScheduler;
import wtf.socket.secure.delegate.WTFSocketSecureDelegatesGroup;

import javax.annotation.Resource;

/**
 * WTFSocket服务器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public final class WTFSocketServer {

    /**
     * Spring 上下文
     */
    private ApplicationContext spring = new ClassPathXmlApplicationContext("spring.wtf.socket.xml");

    /**
     * 消息调度组件
     * 根据消息的头信息将消息投递到指定的目的地
     */
    @Resource()
    private WTFSocketScheduler scheduler;

    /**
     * 协议族组件
     * IO层收到数据后选择合适的解析器将数据解析为标准消息格式
     */
    @Resource
    private WTFSocketProtocolFamily protocolFamily;

    /**
     * 安全组件
     * 可用添加一些安全策略，如发送数据的授权许可等
     */
    @Resource
    private WTFSocketSecureDelegatesGroup secureDelegatesGroup;

    /**
     * 路由组件
     * 查询和记录连接的地址
     */
    @Resource
    private WTFSocketRouting routing;

    /**
     * 事件组组件
     * 包含了一些服务器的监听事件
     */
    @Resource
    private WTFSocketEventListenersGroup eventsGroup;

    /**
     * 服务器配置
     */
    private WTFSocketConfig config;

    public WTFSocketServer() {
        spring.getAutowireCapableBeanFactory().autowireBean(this);
        scheduler.setContext(this);
        routing.setContext(this);
    }

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

    public WTFSocketSecureDelegatesGroup getSecureDelegatesGroup() {
        return secureDelegatesGroup;
    }

    public WTFSocketRouting getRouting() {
        return routing;
    }

    public WTFSocketEventListenersGroup getEventsGroup() {
        return eventsGroup;
    }

    public WTFSocketConfig getConfig() {
        return config;
    }

    public ApplicationContext getSpring() {
        return spring;
    }
}
