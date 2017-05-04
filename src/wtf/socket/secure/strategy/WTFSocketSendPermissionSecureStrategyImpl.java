package wtf.socket.secure.strategy;

import org.springframework.stereotype.Component;
import wtf.socket.exception.normal.WTFSocketPermissionDeniedException;
import wtf.socket.routing.client.WTFSocketFormalClient;
import wtf.socket.secure.delegate.WTFSocketSecureDelegate;
import wtf.socket.secure.delegate.WTFSocketSecureDelegateType;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 * 消息是否有权限进行发送
 * <p>
 * Created by ZFly on 2017/4/22.
 */
@Component
public final class WTFSocketSendPermissionSecureStrategyImpl extends WTFSocketSecureStrategySupport  {

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {

        if (null != request.getException()) {
            return;
        }

        for (WTFSocketResponseData responseData : response.getResponseDataList()) {

            final String targetAddress = responseData.getMessage().getTo();
            final String sourceAddress = responseData.getMessage().getFrom();

            if (null != responseData.getException()) {
                logger.debug("[" + targetAddress + "] SendPermission check bypass");
                continue;
            }

            final WTFSocketFormalClient source = (WTFSocketFormalClient) context.getRouting().getItem(responseData.getMessage().getFrom());
            final WTFSocketSecureDelegate authDelegate = response.getContext().getSecureDelegateGroup().getDelegate(WTFSocketSecureDelegateType.SEND_PERMISSION);

            // 权限校验
            if (!source.isAuthTarget(targetAddress)) {
                if (authDelegate == null || (boolean) authDelegate.work(responseData.getMessage())) {
                    source.addAuthTarget(targetAddress);
                } else {
                    logger.debug("[" + targetAddress + "] SendPermission check failure");
                    responseData.setException(new WTFSocketPermissionDeniedException("[" + sourceAddress + "] has no permission to send message to [" + targetAddress + "]"));
                }
            }else {
                logger.debug("[" + targetAddress + "] SendPermission check through");
            }
        }
    }
}
