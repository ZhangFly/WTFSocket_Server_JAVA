package wtf.socket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wtf.socket.event.WTFSocketEventsGroup;
import wtf.socket.protocol.WTFSocketProtocolFamily;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.schedule.WTFSocketConfig;
import wtf.socket.schedule.WTFSocketScheduler;
import wtf.socket.secure.WTFSocketSecure;

/**
 * WTFSocket 组件列表
 *
 * SCHEDULER 消息调度组件
 * PROTOCOL_FAMILY 协议族组件
 * SECURE 安全组件
 * ROUTING 路由组件
 *
 * Created by zfly on 2017/4/25.
 */
public interface WTFSocket {

    ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring.wtf.socket.xml");

    /**
     * 消息调度组件
     * 根据消息的头信息将消息投递到指定的目的地
     */
    WTFSocketScheduler SCHEDULER = (WTFSocketScheduler) CONTEXT.getBean("wtf.socket.scheduler");

    /**
     * 协议族组件
     * IO层收到数据后选择合适的解析器将数据解析为标准消息格式
     */
    WTFSocketProtocolFamily PROTOCOL_FAMILY = (WTFSocketProtocolFamily) CONTEXT.getBean("wtf.socket.protocolFamily");

    /**
     * 安全组件
     * 可用添加一些安全策略，如发送数据的授权许可等
     */
    WTFSocketSecure SECURE = (WTFSocketSecure) CONTEXT.getBean("wtf.socket.secure");

    /**
     * 路由组件
     * 查询和记录连接的地址
     */
    WTFSocketRouting ROUTING = (WTFSocketRouting) CONTEXT.getBean("wtf.socket.routing");

    /**
     * 事件组件
     */
    WTFSocketEventsGroup EVENTS_GROUP = (WTFSocketEventsGroup) CONTEXT.getBean("wtf.socket.eventsGroup");


    static void run(WTFSocketConfig config) {
        SCHEDULER.run(config);
    }
}
