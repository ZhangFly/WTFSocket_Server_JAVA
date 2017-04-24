package wtf.socket.routing;

import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 路由表清洁器
 * 将连接到服务器，但长时间未完成注册的连接清理
 */
public class WTFSocketCleaner {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void runExpire() {
        executor.scheduleAtFixedRate(
                () -> WTFSocketRoutingMap.TMP.mapValues().stream()
                        .filter(item -> ((WTFSocketRoutingTmpItem) item).isExpires())
                        .forEach(item -> {
                            item.getTerm().close();
                            WTFSocketRoutingMap.TMP.unRegister(item);
                            System.out.println("< " + item.getAddress() + " > expires");
                        }),
                1, 1, TimeUnit.MINUTES);
    }
}
