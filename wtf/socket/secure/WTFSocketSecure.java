package wtf.socket.secure;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.secure.strategy.*;

/**
 *
 * Created by zfly on 2017/4/24.
 */
@Component("wtf.socket.secure")
public class WTFSocketSecure {

    public WTFSocketSecureDelegate getSendPermissionAuthDelegate() {
        return ((WTFSocketSendPermissionStrategy)WTFSocket.CONTEXT.getBean("wtf.socket.secure.strategy.sendPermission")).getAuthDelegate();
    }

    public void setSendPermissionAuthDelegate(WTFSocketSecureDelegate authDelegate) {
        ((WTFSocketSendPermissionStrategy)WTFSocket.CONTEXT.getBean("wtf.socket.secure.strategy.sendPermission")).setAuthDelegate(authDelegate);
    }
}
