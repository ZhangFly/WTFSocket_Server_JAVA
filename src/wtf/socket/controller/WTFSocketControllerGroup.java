package wtf.socket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.workflow.*;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketRichResponse;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 控制器组
 * <p>
 * Created by ZFly on 2017/4/29.
 */
@Component
public class WTFSocketControllerGroup implements WTFSocketWork {

    private final Log logger = LogFactory.getLog(WTFSocketControllerGroup.class);

    private Queue<WTFSocketSimpleController> controllers = new PriorityQueue<>(Comparator.comparingInt(WTFSocketSimpleController::priority));
    private WTFSocketControllerGroup dependence = null;
    @Resource
    private WTFSocketServer context;

    WTFSocketControllerGroup() {}

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
    public static WTFSocketControllerGroup depends(WTFSocketControllerGroup dependence) {
        return dependence.upgrade();
    }


    /**
     * 从Spring已注册的Bean中添加控制器
     */
    public void addControllerFromSpringBeans() {
        context.getSpring().getBeansOfType(WTFSocketSimpleController.class)
                .forEach((key, value) -> add(value));
    }

    /**
     * 添加一个控制器
     *
     * @param controller 控制器
     *
     * @return 控制器组自身
     */
    public WTFSocketControllerGroup add(WTFSocketSimpleController controller) {
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
    public WTFSocketControllerGroup upgrade() {
        final WTFSocketControllerGroup upgradeGroup = new WTFSocketControllerGroup();
        upgradeGroup.dependence = this;
        return upgradeGroup;
    }

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        if (null == request.getMessage()) {
            return;
        }

        logger.debug("WTFSocketControllerGroup work");
        try {
            for (WTFSocketSimpleController controller : controllers) {
                if (controller.isResponse(request.getMessage())) {
                    logger.debug("Request triggered controller [" + controller.getClass().getSimpleName() + "]");

                    if (controller.work(request.getSourceClient(), request.getMessage(), response)) {
                        logger.debug("Controller [" + controller.getClass().getSimpleName() + "] consumed the request");
                        logger.debug("Response contained [" + response.getResponseDataList().size() + "] pieces of message");
                        break;
                    }
                }
            }
        } catch (WTFSocketException e) {
            request.setException(e);
        }

        if (null != dependence) {
            dependence.execute(request, response);
        }

        logger.debug("WTFSocketControllerGroup finish");
    }
}
