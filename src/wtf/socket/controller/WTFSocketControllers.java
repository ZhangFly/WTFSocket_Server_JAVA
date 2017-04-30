package wtf.socket.controller;

import wtf.socket.controller.impl.WTFSocketDebugRegisterController;
import wtf.socket.controller.impl.WTFSocketEchoController;
import wtf.socket.controller.impl.WTFSocketMsgForwardingController;
import wtf.socket.controller.impl.WTFSocketUnconditionalRegisterController;

/**
 * 提供常用的控制器
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public final class WTFSocketControllers {

    private WTFSocketControllers() {
    }

    /**
     * 消息转发控制器
     * 优先级为 WTFSocketPriority.LOWEST
     * 不消费请求
     *
     * @return 控制器单例
     */
    public static WTFSocketController msgForwardingController() {
        return WTFSocketMsgForwardingController.INSTANCE;
    }

    /**
     * 无条件的注册控制器
     * 即不做任何授权检查即将发消息的临时客户端注册为正式客户端
     * 优先级为 WTFSocketPriority.HIGHEST
     * 不消费请求
     *
     * @return 控制器单例
     */
    public static WTFSocketController unconditionalRegisterController() {
        return WTFSocketUnconditionalRegisterController.INSTANCE;
    }

    /**
     * 调试客户端注册控制器
     * 将通讯地址以 Debug_ 开头的临时客户端注册为调试控制端
     * 优先级为 WTFSocketPriority.HIGHEST
     * 不消费请求
     *
     * @return 控制器单例
     */
    public static WTFSocketController debugRegisterController() {
        return WTFSocketDebugRegisterController.INSTANCE;
    }

    /**
     * 回声控制器
     * 将消息的 From 和 To 对调后返回客户端
     * 优先级为 WTFSocketPriority.MEDIUM
     * 消费请求
     *
     * @return 控制器单例
     */
    public static WTFSocketController echoController() {
        return WTFSocketEchoController.INSTANCE;
    }
}
