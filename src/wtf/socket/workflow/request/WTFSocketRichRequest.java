package wtf.socket.workflow.request;

import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketRichRequest extends WTFSocketRequest{

    void setMessage(WTFSocketMessage message);

    void setDataPacket(String dataPacket);

    String getIoTag();

    void setIOTag(String ioTag);

    String getConnectType();

    void setConnectType(String connectType);

    boolean isSecure();

    void setSecure(boolean secure);

    WTFSocketException getException();

    void setException(WTFSocketException exception);
}
