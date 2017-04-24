package wtf.socket.schedule;

import wtf.socket.protocol.WTFSocketMsg;

import java.util.List;

public interface WTFSocketHandler {

    void invoke(WTFSocketMsg request, List<WTFSocketMsg> responses);

}
