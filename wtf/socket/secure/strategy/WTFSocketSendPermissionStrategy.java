package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.exception.normal.WTFSocketPermissionDeniedException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.secure.WTFSocketSecureStrategy;
import wtf.socket.secure.WTFSocketSecureDelegate;

/**
 * 授权器接口
 *
 * Created by zfly on 2017/4/22.
 */
@Component("wtf.socket.secure.strategy.sendPermission")
public class WTFSocketSendPermissionStrategy implements WTFSocketSecureStrategy {

    private WTFSocketSecureDelegate authDelegate;

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketPermissionDeniedException {

        final WTFSocketRoutingFormalItem source = WTFSocket.ROUTING.FORMAL_MAP.getItem(msg.getTo());

        // 权限校验
        if (!StringUtils.equals(msg.getFrom(), "server") && !source.isAuthTarget(msg.getTo())) {
            if (authDelegate == null || (boolean) authDelegate.work(msg)) {
                source.addAuthTarget(msg.getTo());
            } else {
                throw new WTFSocketPermissionDeniedException(msg.getTo()) {{
                    setOriginalMsg(msg);
                }};
            }
        }
    }

    public WTFSocketSecureDelegate getAuthDelegate() {
        return authDelegate;
    }

    public void setAuthDelegate(WTFSocketSecureDelegate authDelegate) {
        this.authDelegate = authDelegate;
    }
}
