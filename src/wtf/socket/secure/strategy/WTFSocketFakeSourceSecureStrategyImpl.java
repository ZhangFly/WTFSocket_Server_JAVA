package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 发送源是通讯地址和连接io地址是否匹配
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketFakeSourceSecureStrategyImpl extends WTFSocketSecureStrategySupport{

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        if (null != request.getException()) {
            return;
        }

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            final String sourceAddress = responseData.getMessage().getFrom();

            if (null != responseData.getException()) {
                logger.debug("[" + sourceAddress + "] FakeSourceSecure check bypass");
                continue;
            }

            if (!StringUtils.equals(context.getRouting().getItem(sourceAddress).getTerm().getIoTag(), responseData.getMessage().getIoTag())) {
                logger.debug("[" + sourceAddress + "] FakeSourceSecure check failure");
                responseData.setException(new WTFSocketInvalidSourceException("[" + sourceAddress + "] communication address miss match io address"));
            }else {
                logger.debug("[" + sourceAddress + "] FakeSourceSecure check through");
            }
        }
    }
}
