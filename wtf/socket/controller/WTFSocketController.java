package wtf.socket.controller;

import org.springframework.stereotype.Controller;
import wtf.socket.protocol.WTFSocketMsg;

import java.util.List;

/**
 * 服务器功能接口
 */
@Controller
public interface WTFSocketController {

    boolean isResponse(WTFSocketMsg msg);

    void work(WTFSocketMsg request, List<WTFSocketMsg> responses);

}
