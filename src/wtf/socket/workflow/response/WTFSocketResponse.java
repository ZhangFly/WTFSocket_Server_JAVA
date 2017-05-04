package wtf.socket.workflow.response;

import wtf.socket.protocol.WTFSocketMessage;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketResponse {

    void addMessage(WTFSocketMessage message);

    boolean isEmpty();
}
