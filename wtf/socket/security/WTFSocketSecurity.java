package wtf.socket.security;

import org.apache.commons.lang.StringUtils;
import wtf.socket.exception.WTFSocketInvalidSourceException;
import wtf.socket.exception.WTFSocketInvalidTargetException;
import wtf.socket.exception.WTFSocketPermissionDeniedException;
import wtf.socket.inter.delegate.WTFSocketAuthDelegate;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.WTFSocketRoutingMap;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;

/**
 *
 * Created by zfly on 2017/4/24.
 */
public abstract class WTFSocketSecurity {

    private static WTFSocketAuthDelegate authDelegate = (from, to) -> true;


    public static WTFSocketAuthDelegate getAuthDelegate() {
        return authDelegate;
    }

    public static void setAuthDelegate(WTFSocketAuthDelegate authDelegate) {
        WTFSocketSecurity.authDelegate = authDelegate;
    }

    /**
     * 移除授权代理
     */
    public static void removeAuthDelegate() {
        authDelegate = ((from, to) -> true);
    }

    public static void check(WTFSocketMsg msg) throws WTFSocketInvalidSourceException, WTFSocketInvalidTargetException, WTFSocketPermissionDeniedException {
        final String toAddress = msg.getTo();
        final String fromAddress = msg.getFrom();
        final String fromIoTag = msg.getIoTag();

        // 无效源地址
        if (!WTFSocketRoutingMap.FORMAL.contains(fromAddress))
            throw new WTFSocketInvalidSourceException(toAddress);

        final WTFSocketRoutingFormalItem source = (WTFSocketRoutingFormalItem) WTFSocketRoutingMap.FORMAL.getItem(fromAddress);

        // 源地址与ioTag不匹配
        if (!StringUtils.equals(source.getTerm().getIoTag(), fromIoTag))
            throw new WTFSocketInvalidSourceException(fromAddress);

        // 无效目标地址
        if (!WTFSocketRoutingMap.FORMAL.contains(toAddress))
            throw (WTFSocketInvalidTargetException) new WTFSocketInvalidTargetException(toAddress).setOriginalMsg(msg);

        // 权限校验
        if (!StringUtils.equals(fromAddress, "server") && !source.isAuthTarget(fromAddress)) {
            if (authDelegate.invoke(fromAddress, toAddress)) {
                source.addAuthTarget(toAddress);
            } else {
                throw (WTFSocketPermissionDeniedException) new WTFSocketPermissionDeniedException(toAddress).setOriginalMsg(msg);
            }
        }
    }
}
