package wtf.socket.secure.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 发送源是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketContainsSourceSecureStrategyImpl extends WTFSocketSecureStrategySupport {

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        if (null != request.getException()) {
            return;
        }

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            final String sourceAddress = responseData.getMessage().getFrom();

            if (!context.getRouting().getFormalMap().contains(sourceAddress) && !context.getRouting().getDebugMap().contains(sourceAddress)) {
                logger.debug("[" + sourceAddress + "] ContainsSource check failure");
                responseData.setException(new WTFSocketInvalidSourceException("Source [" + sourceAddress + "] was never registered"));
            } else {
                logger.debug("[" + sourceAddress+ "] ContainsSource check through");
            }
        }
    }
}
