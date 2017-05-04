package wtf.socket.secure.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.workflow.WTFSocketWork;

import javax.annotation.Resource;

/**
 *
 * Created by ZFly on 2017/5/4.
 */
@Component
public abstract class WTFSocketSecureStrategySupport implements WTFSocketWork {

    protected Log logger = LogFactory.getLog(this.getClass());

    @Resource
    protected WTFSocketServer context;
}
