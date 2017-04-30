package wtf.socket.secure.strategy;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 * 发送源是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.containsSource")
public class WTFSocketContainsSourceStrategy implements WTFSocketSecureStrategy {

    @Override
    public void execute(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketInvalidSourceException {
        if (!context.getRouting().getFormalMap().contains(msg.getFrom()))
            throw new WTFSocketInvalidSourceException(msg.getFrom());
    }
}
