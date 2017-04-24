package wtf.socket.util;

import org.apache.commons.lang.StringUtils;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.WTFSocketRoutingMap;
import wtf.socket.routing.item.WTFSocketRoutingDebugItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 辅助工具类
 *
 * Created by zfly on 2017/4/21.
 */
public class WTFSocketLogUtils {

    public static void receive(String packet, WTFSocketMsg msg) {
        final String receiveMsg = String.format(
                    "receive<%s> from <%s> to <%s>:\n%s\n",
                    msg.getConnectType(),
                    msg.getFrom(),
                    msg.getTo(),
                    packet);
        if (!StringUtils.startsWith(msg.getFrom(), "Debug_")) {
            debugOutput(receiveMsg);
        }
    }

    public static void dispatch(String packet, WTFSocketMsg msg) {
        final String dispatchMsg = String.format(
                "receive<%s> from <%s> to <%s>:\n%s\n",
                msg.getConnectType(),
                msg.getFrom(),
                msg.getTo(),
                packet);
        if (!StringUtils.startsWith(msg.getTo(), "Debug_")) {
            debugOutput(dispatchMsg);
        }
    }

    public static void exception(String packet, WTFSocketMsg msg) {
        final String exceptionMsg = String.format(
                "exception<%s> from <%s>:\n%s\n",
                msg.getConnectType(),
                msg.getTo(),
                packet);
        if (!StringUtils.startsWith(msg.getTo(), "Debug_")) {
            debugOutput(exceptionMsg);
        }
    }

    private static void debugOutput(String msg) {
        WTFSocketRoutingMap.DEBUG.mapValues().stream()
                .filter(item -> ((WTFSocketRoutingDebugItem) item).isFilter(msg))
                .forEach(item -> {
                    final DateFormat format = new SimpleDateFormat("hh:mm:ss:SSS");
                    final String data = format.format(new Date());
                    final String info = String.format("[%s] %s", data, msg);
                    item.getTerm().write(info);
                });
    }
}
