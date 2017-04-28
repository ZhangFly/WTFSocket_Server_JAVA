package controller;

import com.alibaba.fastjson.JSONObject;
import model.ApplicationMsg;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import wtf.socket.WTFSocket;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;
import wtf.socket.secure.strategy.WTFSocketFakeSourceStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 注册功能
 */
@Controller
public class RegisterController implements WTFSocketController {

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
//        ApplicationMsg body = msg.getBody(ApplicationMsg.class);
//        return body.getCmd() != null &&
//                body.getCmd() == 64;
        return true;
    }

    public void work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException{

        final ApplicationMsg body = request.getBody(ApplicationMsg.class);

        if (!(source instanceof WTFSocketRoutingTmpItem)) {
            throw new WTFSocketInvalidSourceException(request.getFrom());
        }

        source.setAddress(request.getFrom());
        source.setAccept(request.getVersion());

        if (body.hasParams())
            source.setType(body.firstParam().getString("deviceType"));

        if (StringUtils.startsWith(request.getFrom(), "Debug_")) {
            ((WTFSocketRoutingTmpItem) source).shiftToDebug();
        }else {
            ((WTFSocketRoutingTmpItem) source).shiftToFormal();
        }

        final WTFSocketMsg response = request.makeResponse();
        response.setBody(ApplicationMsg.success());
        responses.add(response);

//        if (source instanceof WTFSocketRoutingTmpItem) {
//            source.setAddress(request.getFrom());
//            source.setAccept(request.getVersion());
//            ((WTFSocketRoutingTmpItem) source).shiftToFormal();
//        }
//        WTFSocketMsg helloWorld = request.makeResponse();
//        helloWorld.setBody(new JSONObject() {{
//            put("say", "hello world");
//        }});
//        responses.add(helloWorld);
    }
}
