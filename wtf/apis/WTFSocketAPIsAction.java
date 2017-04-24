package wtf.apis;

import wtf.socket.protocol.WTFSocketMsg;

import java.util.List;

/**
 * 服务器功能接口
 */
@FunctionalInterface
public interface WTFSocketAPIsAction {

    void doAction(WTFSocketMsg request, List<WTFSocketMsg> responses);

}
