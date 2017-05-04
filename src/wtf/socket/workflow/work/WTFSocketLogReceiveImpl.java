package wtf.socket.workflow.work;

import org.springframework.stereotype.Component;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
public class WTFSocketLogReceiveImpl extends WTFSocketLogSupport implements WTFSocketWork{

    WTFSocketLogReceiveImpl() {}

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        logger.debug("WTFSocketLogReceive work");
        final String receiveMsg = String.format(
                "Received[%s] message from [%s] to [%s]:\n%s\n",
                request.getMessage().getConnectType(),
                request.getMessage().getFrom(),
                request.getMessage().getTo(),
                request.getDataPacket());

        if (!request.getContext().getRouting().getDebugMap().contains(request.getMessage().getFrom())) {
            output(receiveMsg);
        }
        logger.debug("WTFSocketLogReceive finish");
    }
}
