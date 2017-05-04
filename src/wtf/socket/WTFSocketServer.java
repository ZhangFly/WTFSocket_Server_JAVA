package wtf.socket;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wtf.socket.controller.WTFSocketControllerGroup;
import wtf.socket.event.WTFSocketEventListenerGroup;
import wtf.socket.protocol.WTFSocketProtocolFamily;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.workflow.WTFSocketWorkflow;
import wtf.socket.secure.delegate.WTFSocketSecureDelegateGroup;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;

/**
 * WTFSocket服务器
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketServer {

    /**
     * Spring 上下文
     */
    private static final ApplicationContext spring = new ClassPathXmlApplicationContext("spring.wtf.socket.xml");

    private WTFSocketServer() {}

    /**
     * 消息调度组件
     * 根据消息的头信息将消息投递到指定的目的地
     */
    @Resource
    private WTFSocketWorkflow workflow;

    @Resource
    private WTFSocketControllerGroup controllerGroup;

    /**
     * 路由组件
     * 查询和记录连接的地址
     */
    @Resource
    private WTFSocketRouting routing;

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
    private WTFSocketSecureDelegateGroup secureDelegateGroup;

    /**
     * 事件组组件
     * 包含了一些服务器的监听事件
     */
    @Resource
    private WTFSocketEventListenerGroup eventGroup;

    /**
     * 服务器配置
     */
    private WTFSocketConfig config;

    public static WTFSocketServer newNettyServer() {
        return spring.getBean(WTFSocketServer.class);
    }
    
    public void run(WTFSocketConfig config) {
        this.config = config;
        workflow.run();
    }

    public void run() {
        run(WTFSocketConfig.createFromProperties((Properties) spring.getBean("config")));
    }

    public WTFSocketWorkflow getWorkflow() {
        return workflow;
    }

    public WTFSocketProtocolFamily getProtocolFamily() {
        return protocolFamily;
    }

    public WTFSocketSecureDelegateGroup getSecureDelegateGroup() {
        return secureDelegateGroup;
    }

    public WTFSocketRouting getRouting() {
        return routing;
    }

    public WTFSocketEventListenerGroup getEventGroup() {
        return eventGroup;
    }

    public WTFSocketConfig getConfig() {
        return config;
    }

    public ApplicationContext getSpring() {
        return spring;
    }

    public WTFSocketControllerGroup getControllerGroup() {
        return controllerGroup;
    }
}
