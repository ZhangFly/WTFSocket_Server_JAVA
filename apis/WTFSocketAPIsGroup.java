package wtf.apis;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketProtocol;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class WTFSocketAPIsGroup {

    private ConcurrentLinkedQueue<WTFSocketAPIsTrigger> apIsTriggers = new ConcurrentLinkedQueue<>();

    public WTFSocketAPIsGroup addAction(WTFSocketAPIsTrigger apIsTrigger, Class<? extends WTFSocketAPIsAction> actionClass) {
        try {
            apIsTrigger.setAction(actionClass.newInstance());
            apIsTriggers.add(apIsTrigger);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    void doAction(Channel ctx, WTFSocketProtocol protocol, List<WTFSocketProtocol> responses) {

        for (WTFSocketAPIsTrigger apIsTrigger : apIsTriggers) {
            if (apIsTrigger.when(protocol)) {
                apIsTrigger.getAction().doAction(ctx, protocol, responses);
                return;
            }
        }
    }
}
