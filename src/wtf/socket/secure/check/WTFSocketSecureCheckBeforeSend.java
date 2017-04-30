package wtf.socket.secure.check;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureCheck;
import wtf.socket.secure.WTFSocketSecureStrategy;

import javax.annotation.Resource;

/**
 * 信息发送前进行的安全检查
 * <p>
 * Created by ZFly on 2017/4/25.
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
    public boolean check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {

        keepWords.execute(context, msg);
        fakeSource.execute(context, msg);
        containsTarget.execute(context, msg);
        sendPermission.execute(context, msg);
        return true;
    }
}
