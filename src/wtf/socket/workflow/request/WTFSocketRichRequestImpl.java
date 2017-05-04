package wtf.socket.workflow.request;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;

import javax.annotation.Resource;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
@Scope("prototype")
public class WTFSocketRichRequestImpl implements WTFSocketRichRequest{

    @Resource
    private WTFSocketServer context;
    private WTFSocketMessage message;
    private String dataPacket;
    private String ioTag;
    private String connectType;
    private boolean secure;
    private WTFSocketException exception;

    WTFSocketRichRequestImpl() {}

    @Override
    public void setMessage(WTFSocketMessage message) {
        this.message = message;
    }

    @Override
    public void setDataPacket(String dataPacket) {
        this.dataPacket = dataPacket;
    }

    @Override
    public String getIoTag() {
        return ioTag;
    }

    @Override
    public void setIOTag(String ioTag) {
        this.ioTag = ioTag;
    }

    @Override
    public String getConnectType() {
        return connectType;
    }

    @Override
    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    @Override
    public WTFSocketServer getContext() {
        return context;
    }

    @Override
    public WTFSocketClient getSourceClient() {
        return context.getRouting().getItem(ioTag);
    }

    @Override
    public WTFSocketClient getTargetClient() {
        return context.getRouting().getItem(message.getTo());
    }

    @Override
    public WTFSocketMessage getMessage() {
        return message;
    }

    @Override
    public String getDataPacket() {
        return dataPacket;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public WTFSocketException getException() {
        return exception;
    }

    @Override
    public void setException(WTFSocketException exception) {
        this.exception = exception;
    }
}
