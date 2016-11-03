package wtf.apis;

import wtf.socket.protocols.templates.WTFSocketProtocol;

import io.netty.channel.Channel;

import java.util.List;

/**
 * 服务器功能接口
 */
public interface WTFSocketAction {

    void doAction(Channel ctx, WTFSocketProtocol protocol, List<WTFSocketProtocol> responses);

}
