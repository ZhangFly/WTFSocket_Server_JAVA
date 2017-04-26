package wtf.socket.schedule;

import wtf.socket.protocol.WTFSocketMsg;

import java.util.List;

public interface WTFSocketHandler {

    void handle(WTFSocketMsg request, List<WTFSocketMsg> responses);

}
