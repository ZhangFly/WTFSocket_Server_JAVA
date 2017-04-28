package wtf.socket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.schedule.WTFSocketHandler;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
@Component("wtf.socket.controllerGroup")
public class WTFSocketControllersGroup implements WTFSocketHandler {

    private static final Log logger = LogFactory.getLog(WTFSocketControllersGroup.class);

    private ConcurrentLinkedQueue<WTFSocketController> controllers = new ConcurrentLinkedQueue<>();
    private WTFSocketControllersGroup dependence = null;

    public void loadSpringConfig() {
        WTFSocket.CONTEXT.getBeansOfType(WTFSocketController.class)
                .forEach((key, value) -> {
                    this.addController(value);
                });
    }

    public WTFSocketControllersGroup addController(WTFSocketController controller) {
        controllers.add(controller);
        logger.info("Map controller [" + controller.getClass().getName() + "]");
        return this;
    }

    public static WTFSocketControllersGroup depends(WTFSocketControllersGroup dependence) {
        final WTFSocketControllersGroup group = new WTFSocketControllersGroup();
        group.dependence = dependence;
        return group;
    }

    @Override
    public void handle(WTFSocketRoutingItem item, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException{
        for (WTFSocketController controller : controllers) {
            if (controller.isResponse(request)) {
                controller.work(item, request, responses);
            }
        }
        if (dependence != null) dependence.handle(item, request, responses);
    }
}
