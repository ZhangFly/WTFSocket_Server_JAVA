package wtf.socket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.schedule.WTFSocketHandler;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 控制器组
 * <p>
 * Created by ZFly on 2017/4/29.
 */
@Component("wtf.socket.controllersGroup")
@Scope("prototype")
public class WTFSocketControllersGroup implements WTFSocketHandler {

    private static final Log logger = LogFactory.getLog(WTFSocketControllersGroup.class);

    private Queue<WTFSocketController> controllers = new PriorityQueue<>(Comparator.comparingInt(WTFSocketController::getPriority));
    private WTFSocketControllersGroup dependence = null;

    /**
     * 在dependence的基础上创建一个新的升级版控制器组
     * 新的控制器组继承所有dependence中已添加的控制器
     * 当升级版控制器中的控制器不响应请求时
     * 会继续在dependence中继续询问
     *
     * @param dependence 老控制器组
     *
     * @return 升级版控制器组
     */
    public static WTFSocketControllersGroup depends(WTFSocketControllersGroup dependence) {
        return dependence.upgrade();
    }


    /**
     * 从Spring已注册的Bean中添加控制器
     */
    public void addControllerFromSpringBeans(WTFSocketServer context) {
        context.getSpring().getBeansOfType(WTFSocketController.class)
                .forEach((key, value) -> addController(value));
    }

    /**
     * 添加一个控制器
     *
     * @param controller 控制器
     *
     * @return 控制器组自身
     */
    public WTFSocketControllersGroup addController(WTFSocketController controller) {
        controllers.add(controller);
        logger.info("WTFSocketServer mapped controller [" + controller.getClass().getName() + "]");
        return this;
    }

    /**
     * 创建一个新的升级版控制器组
     * 新的控制器组继承所有现控制器组中已添加的控制器
     * 当升级版控制器中的控制器不响应请求时
     * 会继续在现控制器中继续询问
     *
     * @return 升级版控制器组
     */
    public WTFSocketControllersGroup upgrade() {
        final WTFSocketControllersGroup upgradeGroup = new WTFSocketControllersGroup();
        upgradeGroup.dependence = this;
        return upgradeGroup;
    }

    @Override
    public void handle(WTFSocketRoutingItem item, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        for (WTFSocketController controller : controllers) {
            if (controller.isResponse(request)) {
                if (controller.work(item, request, responses))
                    break;
            }
        }
        if (dependence != null) dependence.handle(item, request, responses);
    }
}
