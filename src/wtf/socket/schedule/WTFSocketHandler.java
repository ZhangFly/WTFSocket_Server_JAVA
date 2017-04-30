package wtf.socket.schedule;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.List;

/**
 * 调度器服务函数接口
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public interface WTFSocketHandler {

    void handle(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException;

}
