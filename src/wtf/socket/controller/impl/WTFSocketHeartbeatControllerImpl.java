package wtf.socket.controller.impl;

import org.apache.commons.lang.StringUtils;
import wtf.socket.WTFSocketServer;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.secure.strategy.WTFSocketSecureStrategy;
import wtf.socket.secure.strategy.impl.WTFSocketBaseSecureStrategyImpl;
import wtf.socket.util.WTFSocketPriority;

import java.util.List;

/**
 *
 * Created by ZFly on 2017/5/1.
 */
public enum  WTFSocketHeartbeatControllerImpl implements WTFSocketController {

    INSTANCE;

    WTFSocketHeartbeatControllerImpl() {

    }

    private boolean unInit = true;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGH;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return msg.getMsgType() == 0;
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        if (unInit) {
            // 注册heartbeat用户
            final WTFSocketRoutingFormalItem heartbeatItem = new WTFSocketRoutingFormalItem();
            heartbeatItem.setTerm(new WTFSocketDefaultIOTerm());
            heartbeatItem.setContext(source.getContext());
            heartbeatItem.setAddress("heartbeat");
            heartbeatItem.setCover(false);
            heartbeatItem.addAuthTarget("*");
            source.getContext().getRouting().getFormalMap().add(heartbeatItem);
            // 添加安全策略
            source.getContext().getScheduler().getOnReceiveSecureStrategy().setNext(new WTFSocketBaseSecureStrategyImpl() {
                @Override
                public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
                    if (StringUtils.equals("heartbeat", msg.getFrom()))
                        throw new WTFSocketKeepWordsException("Source can not be [heartbeat]");
                    doNext(context, msg);
                }
            });
            unInit = false;
        }

        final WTFSocketMsg response = request.makeResponse();
        response.setBody(request.getBody());
        responses.add(response);
        return true;
    }
}
