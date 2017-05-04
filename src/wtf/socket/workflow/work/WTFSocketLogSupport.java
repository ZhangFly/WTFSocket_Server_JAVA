package wtf.socket.workflow.work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
public abstract class WTFSocketLogSupport {

    protected Log logger = LogFactory.getLog(this.getClass());

    @Resource
    private WTFSocketServer context;

    protected void output(String msg) {
        logger.info(msg);

        if (context.getConfig().isDebug()) {
            final DateFormat format = new SimpleDateFormat("hh:mm:ss,SSS");
            final String date = format.format(new Date());
            context.getRouting().getDebugMap().values().stream()
                    .filter(item -> item.isFilter(msg))
                    .forEach(item -> item.getTerm().write(String.format("[%s] %s - %s", item.getAddress(), date, msg)));
        }
    }
}
