package wtf.socket.secure.check;

import org.springframework.stereotype.Component;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureCheck;
import wtf.socket.secure.WTFSocketSecureStrategy;

import javax.annotation.Resource;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.beforeSend")
public class WTFSocketSecureCheckBeforeSend implements WTFSocketSecureCheck {

    @Resource(name = "wtf.socket.secure.strategy.targetKeepWords")
    private WTFSocketSecureStrategy keepWords;
    @Resource(name = "wtf.socket.secure.strategy.fakeSource")
    private WTFSocketSecureStrategy fakeSource;
    @Resource(name = "wtf.socket.secure.strategy.containsTarget")
    private WTFSocketSecureStrategy containsTarget;
    @Resource(name = "wtf.socket.secure.strategy.sendPermission")
    private WTFSocketSecureStrategy sendPermission;

    @Override
    public boolean check(WTFSocketMsg msg) throws WTFSocketException {
        keepWords.invoke(msg);
        fakeSource.invoke(msg);
        containsTarget.invoke(msg);
        sendPermission.invoke(msg);
        return true;
    }
}
