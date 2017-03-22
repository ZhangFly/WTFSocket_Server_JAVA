package wtf.apis;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketProtocol;

import java.util.List;

/**
 * 服务器功能接口
 */
public interface WTFSocketAPIsAction {

    void doAction(Channel ctx, WTFSocketProtocol protocol, List<WTFSocketProtocol> responses);

}
