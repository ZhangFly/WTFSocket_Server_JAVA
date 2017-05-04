package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 是否使用了系统保留关键字
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketTargetKeepWordsSecureStrategyImpl extends WTFSocketSecureStrategySupport {

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            final String targetAddress = responseData.getMessage().getTo();

            if (null != responseData.getException()) {
                logger.debug("[" + targetAddress + "] TargetKeepWords check bypass");
                continue;
            }

            if (StringUtils.equals("server", targetAddress)) {
                logger.debug("[" + targetAddress + "] TargetKeepWords check failure");
                request.setException(new WTFSocketKeepWordsException("Target can not be [" + targetAddress+ "]"));
                responseData.setException(new WTFSocketKeepWordsException("Target can not be [" + targetAddress + "]"));
            }else {
                logger.debug("[" + targetAddress + "] TargetKeepWords check through");
            }
        }
    }
}
