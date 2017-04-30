package wtf.socket.secure;

import wtf.socket.WTFSocketServer;
import wtf.socket.secure.strategy.WTFSocketSendPermissionStrategy;

/**
 * 安全组件
 * <p>
 * Created by ZFly on 2017/4/24.
 */
public class WTFSocketSecure {

    public WTFSocketSecureDelegate getSendPermissionAuthDelegate() {
        return ((WTFSocketSendPermissionStrategy) WTFSocketServer.SPRING.getBean("wtf.socket.secure.strategy.sendPermission")).getAuthDelegate();
    }

    public void setSendPermissionAuthDelegate(WTFSocketSecureDelegate authDelegate) {
        ((WTFSocketSendPermissionStrategy) WTFSocketServer.SPRING.getBean("wtf.socket.secure.strategy.sendPermission")).setAuthDelegate(authDelegate);
    }
}
