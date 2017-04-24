package wtf.apis;

import wtf.socket.protocol.WTFSocketMsg;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class WTFSocketAPIsGroup {

    private ConcurrentLinkedQueue<WTFSocketAPIsTrigger> apIsTriggers = new ConcurrentLinkedQueue<>();
    private WTFSocketAPIsGroup dependence = null;

    public WTFSocketAPIsGroup addAction(WTFSocketAPIsTrigger apIsTrigger, Class<? extends WTFSocketAPIsAction> actionClass) {
        try {
            apIsTrigger.setAction(actionClass.newInstance());
            apIsTriggers.add(apIsTrigger);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public WTFSocketAPIsGroup depends(WTFSocketAPIsGroup dependence) {
        this.dependence = dependence;
        return this;
    }

    void doAction(WTFSocketMsg request, List<WTFSocketMsg> responses) {

        for (WTFSocketAPIsTrigger apIsTrigger : apIsTriggers) {
            if (apIsTrigger.when(request)) {
                apIsTrigger.getAction().doAction(request, responses);
                return;
            }
        }

        if (dependence != null) {
            dependence.doAction(request, responses);
        }
    }
}
