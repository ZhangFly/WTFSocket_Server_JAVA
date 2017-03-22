package wtf.socket.main;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketProtocol;

import java.util.List;

public interface WTFSocketHandler {

    void invoke(Channel ctx, WTFSocketProtocol request, List<WTFSocketProtocol> responses);

}
