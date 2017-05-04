package wtf.socket.workflow.work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
public class WTFSocketSendMessageImpl implements WTFSocketWork {

    private Log logger = LogFactory.getLog(WTFSocketSendMessageImpl.class);

    WTFSocketSendMessageImpl() {}

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        logger.debug("WTFSocketSendMessage work");
        for (WTFSocketResponseData responseData : response.getResponseDataList()) {
            responseData.getTarget().getTerm().write(responseData.getDataPacket() + response.getContext().getConfig().getFirstEOT());
            if (null != responseData.getException()) {
                logger.debug("Sent exception to [" + responseData.getTarget().getAddress() + "]");

            } else {
                logger.debug("Sent message to [" + responseData.getTarget().getAddress() + "]");
            }
        }
        logger.debug("WTFSocketSendMessage finish");
    }
}
