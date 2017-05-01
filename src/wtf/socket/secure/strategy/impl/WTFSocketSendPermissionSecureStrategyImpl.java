package wtf.socket.secure.strategy.impl;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.normal.WTFSocketPermissionDeniedException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.secure.delegate.WTFSocketSecureDelegate;
import wtf.socket.secure.delegate.WTFSocketSecureDelegateType;

/**
 * 消息是否有权限进行发送
 * <p>
 * Created by ZFly on 2017/4/22.
 */
@Component
public final class WTFSocketSendPermissionSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        final WTFSocketRoutingFormalItem source = context.getRouting().getFormalMap().getItem(msg.getFrom());
        final WTFSocketSecureDelegate authDelegate = context.getSecureDelegatesGroup().getDelegate(WTFSocketSecureDelegateType.SEND_PERMISSION);
        // 权限校验
        if (!source.isAuthTarget(msg.getTo())) {
            if (authDelegate == null || (boolean) authDelegate.work(msg)) {
                source.addAuthTarget(msg.getTo());
            } else {
                throw new WTFSocketPermissionDeniedException(msg.getTo()).setOriginalMsg(msg);
            }
        }

        doNext(context, msg);
    }
}
