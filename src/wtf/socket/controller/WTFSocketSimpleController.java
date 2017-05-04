package wtf.socket.controller;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.workflow.request.WTFSocketRequest;
import wtf.socket.workflow.response.WTFSocketResponse;

import java.util.List;

/**
 * 服务器功能接口
 * <p>
 * Created by ZFly on 2017/4/21.
 */
public interface WTFSocketSimpleController extends WTFSocketController {

    @Override
    default boolean work(WTFSocketRequest request, WTFSocketResponse response) throws WTFSocketException {
        return work(request.getSourceClient(), request.getMessage(), response);
    }

    /**
     * 控制器工作函数
     * 当控制器的isResponse()方法为true时调用
     * 当控制器工作完成后如果返回true表示该请求被消费，请求传递终止
     * 否则请求继续向下传递
     *
     * @param source    消息发送源
     * @param request   请求消息
     * @param responses 回复消息列表
     *
     * @return 请求是否被消费
     *
     * @throws WTFSocketException 异常消息
     */
    boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException;
}
