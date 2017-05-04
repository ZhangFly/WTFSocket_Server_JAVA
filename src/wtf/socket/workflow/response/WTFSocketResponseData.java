package wtf.socket.workflow.response;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.normal.WTFSocketNormalException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public class WTFSocketResponseData {

    private WTFSocketMessage message;
    private WTFSocketClient target;
    private String dataPacket;
    private WTFSocketException exception;

    public WTFSocketResponseData(WTFSocketMessage message) {
        this.message = message;
    }

    public WTFSocketMessage getMessage() {
        return message;
    }

    public void setMessage(WTFSocketMessage message) {
        this.message = message;
    }

    public WTFSocketClient getTarget() {
        return target;
    }

    public void setTarget(WTFSocketClient target) {
        this.target = target;
    }

    public String getDataPacket() {
        return dataPacket;
    }

    public void setDataPacket(String dataPacket) {
        this.dataPacket = dataPacket;
    }

    public WTFSocketException getException() {
        return exception;
    }

    public void setException(WTFSocketException exception) {
        this.exception = exception;
    }
}
