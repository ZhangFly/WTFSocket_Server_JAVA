package wtf.socket.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocket;
import wtf.socket.protocol.WTFSocketMsg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 辅助工具类
 *
 * Created by zfly on 2017/4/21.
 */
public class WTFSocketLogUtils {

    private static final Log logger = LogFactory.getLog(WTFSocketLogUtils.class);

    public static void receive(String packet, WTFSocketMsg msg) {
        final String receiveMsg = String.format(
                    "Receive<%s> from <%s> to <%s>:\n%s\n",
                    msg.getConnectType(),
                    msg.getFrom(),
                    msg.getTo(),
                    packet);
        if (!StringUtils.startsWith(msg.getFrom(), "Debug_")) {
            debugOutput(receiveMsg, 1);
        }
    }

    public static void send(String packet, WTFSocketMsg msg) {
        final String dispatchMsg = String.format(
                "Send<%s> from <%s> to <%s>:\n%s\n",
                msg.getConnectType(),
                msg.getFrom(),
                msg.getTo(),
                packet);
        if (!StringUtils.startsWith(msg.getTo(), "Debug_")) {
            debugOutput(dispatchMsg, 1);
        }
    }

    public static void exception(String packet, WTFSocketMsg msg) {
        final String exceptionMsg = String.format(
                "Exception<%s> from <%s>:\n%s\n",
                msg.getConnectType(),
                msg.getTo(),
                packet);
        if (!StringUtils.startsWith(msg.getTo(), "Debug_")) {
            debugOutput(exceptionMsg, 2);
        }
    }

    private static void debugOutput(String msg, int level) {
        final DateFormat format = new SimpleDateFormat("hh:mm:ss,SSS");
        final String date = format.format(new Date());
        if (level == 1)
            logger.info(msg);
        WTFSocket.ROUTING.DEBUG_MAP.values().stream()
                .filter(item -> item.isFilter(msg))
                .forEach(item -> {
                    item.getTerm().write(String.format("[%s] %s - %s", item.getAddress(), date, msg));
                });
    }
}
