package wtf.socket.controller;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.List;

/**
 * 控制器接口
 */
public interface WTFSocketController {

    /**
     * 是否响应该请求
     *
     * @param request 请求消息
     * @return 是否响应
     */
    boolean isResponse(WTFSocketMsg request);

    /**
     * 执行工作
     *
     * @param source 请求发送者
     * @param request 请求消息
     * @param responses 回复消息数组
     * @throws WTFSocketException 发送异常
     */
    void work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException;

}
