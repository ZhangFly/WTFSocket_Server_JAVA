package wtf.apis;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketProtocol;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class APIsGroup {

    private ConcurrentLinkedQueue<APIsTrigger> apIsTriggers = new ConcurrentLinkedQueue<>();

    public APIsGroup addAction(APIsTrigger apIsTrigger, Class<? extends Action> actionClass) {
        try {
            apIsTrigger.setAction(actionClass.newInstance());
            apIsTriggers.add(apIsTrigger);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    void doAction(Channel ctx, WTFSocketProtocol protocol, List<WTFSocketProtocol> responses) {

        for (APIsTrigger apIsTrigger : apIsTriggers) {
            if (apIsTrigger.when(protocol)) {
                apIsTrigger.getAction().doAction(ctx, protocol, responses);
                return;
            }
        }
    }
}
