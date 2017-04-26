package wtf.socket.schedule;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 路由表清洁器
 * 将连接到服务器，但长时间未完成注册的连接清理
 */
@Component("wtf.socket.cleaner")
public class WTFSocketCleaner {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public void work() {
        executor.scheduleAtFixedRate(
                () -> WTFSocket.ROUTING.TMP_MAP.values().stream()
                        .filter(WTFSocketRoutingTmpItem::isExpires)
                        .forEach(item -> {
                            item.getTerm().close();
                            WTFSocket.ROUTING.TMP_MAP.remove(item);
                        }),
                1, 1, TimeUnit.MINUTES);
    }
}
