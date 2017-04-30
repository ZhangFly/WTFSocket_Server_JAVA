package wtf.socket.secure.check;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureCheck;
import wtf.socket.secure.WTFSocketSecureStrategy;

import javax.annotation.Resource;

/**
 * 接收到消息时进行的安全检查
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.onReceive")
public class WTFSocketSecureCheckOnReceive implements WTFSocketSecureCheck {

    @Resource(name = "wtf.socket.secure.strategy.sourceKeepWords")
    private WTFSocketSecureStrategy keepWords;

    @Resource(name = "wtf.socket.secure.strategy.fakeTarget")
    private WTFSocketSecureStrategy fakeTarget;

    @Override
    public boolean check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        keepWords.execute(context, msg);
        fakeTarget.execute(context, msg);
        return true;
    }
}
