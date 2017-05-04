package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 是否使用了系统保留关键字
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketSourceKeepWordsSecureStrategyImpl extends WTFSocketSecureStrategySupport {

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {
        if (null != request.getMessage()) {
            final String sourceAddress = request.getMessage().getFrom();

            if (StringUtils.equals("server", sourceAddress)
                    || StringUtils.equals("heartbeat", sourceAddress)) {

                logger.debug("[" + sourceAddress + "] SourceKeepWords check failure");
                request.setException(new WTFSocketKeepWordsException("Source can not be [" + sourceAddress + "]"));
            } else {
                logger.debug("[" + sourceAddress+ "] SourceKeepWords check success");
            }
        }
    }
}
