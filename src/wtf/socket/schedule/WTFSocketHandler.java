package wtf.socket.schedule;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.List;

public interface WTFSocketHandler {

    void handle(WTFSocketRoutingItem item, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException;

}
