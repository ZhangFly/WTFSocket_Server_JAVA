package wtf.socket.workflow.work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

import java.util.HashMap;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
public class WTFSocketLocateMessageImpl implements WTFSocketWork {

    private static final Log logger = LogFactory.getLog(WTFSocketLocateMessageImpl.class);

    WTFSocketLocateMessageImpl() {}

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        logger.debug("WTFSocketLocateTarget work");

        final WTFSocketRouting routing = request.getContext().getRouting();

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            if (null != responseData.getException()) {
                final WTFSocketMessage errMessage = responseData.getMessage().makeResponse();
                errMessage.setState(responseData.getException().getErrCode());
                errMessage.setBody(new HashMap<String, String>() {{
                    put("cause", responseData.getException().getMessage());
                }});

                WTFSocketClient target = null;
                if (responseData.getException() instanceof WTFSocketInvalidSourceException) {
                    target = routing.getItem(request.getIoTag());
                }else {
                    target = routing.getItem(errMessage.getTo());
                }
                target = target == null ? request.getSourceClient() : target;

                if (null != target.getAccept()) {
                    errMessage.setVersion(target.getAccept());
                }

                responseData.setTarget(target);
                responseData.setMessage(errMessage);

                logger.debug("Located exception [" + responseData.getMessage().getTo() + "]");
            } else {
                final WTFSocketClient target = routing.getItem(responseData.getMessage().getTo());
                responseData.getMessage().setVersion(target.getAccept());
                responseData.setTarget(target);
                logger.debug("Located message [" + responseData.getMessage().getTo() + "]");
            }
        }
        logger.debug("WTFSocketLocateTarget Finish");
    }
}
