package wtf.socket.workflow.request;

import wtf.socket.WTFSocketServer;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketRequest {

    WTFSocketServer getContext();

    WTFSocketClient getSourceClient();

    WTFSocketClient getTargetClient();

    WTFSocketMessage getMessage();

    String getDataPacket();
}
