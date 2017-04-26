package wtf.socket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.schedule.WTFSocketHandler;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
@Component("wtf.socket.controllerGroup")
@Scope("prototype")
public class WTFSocketControllerGroup implements WTFSocketHandler {

    private static final Log logger = LogFactory.getLog(WTFSocketControllerGroup.class);

    private ConcurrentLinkedQueue<WTFSocketController> controllers = new ConcurrentLinkedQueue<>();
    private WTFSocketControllerGroup dependence = null;

    public void loadSpringConfig() {
        WTFSocket.CONTEXT.getBeansOfType(WTFSocketController.class)
                .forEach((key, value) -> {
                    this.addController(value);
                });
    }

    public WTFSocketControllerGroup addController(WTFSocketController controller) {
        controllers.add(controller);
        logger.info("Map controller [" + controller.getClass().getName() + "]");
        return this;
    }

    public static WTFSocketControllerGroup depends(WTFSocketControllerGroup dependence) {
        final WTFSocketControllerGroup group = new WTFSocketControllerGroup();
        group.dependence = dependence;
        return group;
    }

    @Override
    public void handle(WTFSocketMsg request, List<WTFSocketMsg> responses) {
        controllers.stream()
                .filter(action -> action.isResponse(request))
                .forEach(action -> action.work(request, responses));

        if (dependence != null) dependence.handle(request, responses);
    }
}
