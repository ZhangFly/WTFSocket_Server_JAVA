package wtf.socket.secure.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 发送目标是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketContainsTargetSecureStrategyImpl extends WTFSocketSecureStrategySupport {

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        if (null != request.getException()) {
            return;
        }

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            final String targetAddress = responseData.getMessage().getTo();

            if (null != responseData.getException()) {
                logger.debug("[" + targetAddress + "] ContainsTarget check bypass");
                continue;
            }

            if (!context.getRouting().getFormalMap().contains(targetAddress)
                && !context.getRouting().getDebugMap().contains(targetAddress)) {
                logger.debug("[" + targetAddress + "] ContainsTarget check failure");
                responseData.setException(new WTFSocketInvalidTargetException("Target [" + targetAddress + "] was never registered"));
            }else {
                logger.debug("[" + targetAddress+ "] ContainsTarget check through");
            }
        }
    }
}
