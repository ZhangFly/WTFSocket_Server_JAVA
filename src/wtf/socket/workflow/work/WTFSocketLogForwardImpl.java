package wtf.socket.workflow.work;

import org.springframework.stereotype.Component;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**\
 * Created by ZFly on 2017/5/3.
 */
@Component
public class WTFSocketLogForwardImpl extends WTFSocketLogSupport implements WTFSocketWork {

    WTFSocketLogForwardImpl() {}

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        logger.debug("WTFSocketLogForward work");
        for (WTFSocketResponseData responseData : response.getResponseDataList()) {
            String forwardMessage;

            if (null != responseData.getException()) {
                forwardMessage = String.format(
                        "Exception[%s] on message from [%s] to [%s]:\n%s\n",
                        responseData.getMessage().getConnectType(),
                        responseData.getMessage().getFrom(),
                        responseData.getMessage().getTo(),
                        responseData.getException().getMessage());
            } else {
                forwardMessage = String.format(
                        "Forwarded[%s] message from [%s] to [%s]:\n%s\n",
                        responseData.getMessage().getConnectType(),
                        responseData.getMessage().getFrom(),
                        responseData.getMessage().getTo(),
                        responseData.getDataPacket());
            }

            if (!response.getContext().getRouting().getDebugMap().contains(responseData.getMessage().getTo())) {
                output(forwardMessage);
            }
        }
        logger.debug("WTFSocketLogForward finish");
    }
}
